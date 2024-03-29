package com.anaa.dynamicisland.ui.activity

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anaa.dynamicisland.AccessbilityStaticClass
import com.anaa.dynamicisland.BuildConfig
import com.anaa.dynamicisland.MainActivity
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.databinding.ActivityIslandBinding
import com.anaa.dynamicisland.fragments.SimpleDialog
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class IslandActivity : DaggerAppCompatActivity() {

    lateinit var binding:ActivityIslandBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this,viewModelFactory)[IslandViewModel::class.java]
    }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            binding.cross.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.check))
        } else {
            binding.cross.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.xcross))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIslandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(viewModel.isSetupDone()) {
            initClickListener()
            setupBluetoothview()
        }else{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        binding.version.text = BuildConfig.VERSION_NAME
    }

    private fun initClickListener() {
        binding.demo.setOnClickListener {
            try {
                AccessbilityStaticClass.service?.updateState(NotchIslandStateSealedClass.ChargingNotch(0,null))
            }catch (e:Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }

        }
        binding.ringerDemo.setOnClickListener {
            try {
                AccessbilityStaticClass.service?.updateState(NotchIslandStateSealedClass.BluetoothConnected(null))
            }catch (e:Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }

        }
        binding.dynamicSwitch.setOnCheckedChangeListener { _, b -> setupChangeListener(b)}
        binding.bluetoothPermission.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_CONNECT) == 0) {
                return@setOnClickListener
            }
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        }
        binding.notificationListenerService.setOnClickListener {
            if(notificationListenerEnabled()) {
                return@setOnClickListener
            }
            openNotificationListenerSettings()
        }
    }

    private fun setupChangeListener(b: Boolean) {
        if(b) {
            if(!checkAccessibilityPermission()) {
                startDialogFragment()
                val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager?
                am!!.addAccessibilityStateChangeListener { b ->
                    if(b) {
                        startActivity(Intent(this,IslandActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                    }
                }
                return
            }
            binding.demo.isEnabled = true
            binding.ringerDemo.isEnabled = true
            viewModel.setMannuallyChanged(true)
            AccessbilityStaticClass.service?.enableView()
        } else {
            binding.demo.isEnabled = false
            binding.ringerDemo.isEnabled = false
            viewModel.setMannuallyChanged(true)
            AccessbilityStaticClass.service?.removeView()
        }
    }


    private fun startDialogFragment() {
        SimpleDialog.newInstance("Accessibility", getString(R.string.message)).apply {
            buttonInterface = object : SimpleDialog.onButtonClick {
                override fun okClicked() {
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }

                override fun cancelClicked() {

                }

            }
        }.show(supportFragmentManager, SimpleDialog.TAG)
    }

    private fun checkAccessibilityPermission(): Boolean {
        var isAccessibilityEnabled = false
        (getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).apply {
            installedAccessibilityServiceList.forEach { installedService ->
                installedService.resolveInfo.serviceInfo.apply {
                    if (getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK).any { it.resolveInfo.serviceInfo.packageName == packageName && it.resolveInfo.serviceInfo.name == name && permission == Manifest.permission.BIND_ACCESSIBILITY_SERVICE && it.resolveInfo.serviceInfo.packageName == packageName })
                        isAccessibilityEnabled = true
                }
            }
        }
        return isAccessibilityEnabled
    }

    private fun startTimer() {
        lifecycleScope.launch {
            delay(3000)
            AccessbilityStaticClass.service?.updateState(NotchIslandStateSealedClass.DefaultNotch)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.dynamicSwitch.setOnCheckedChangeListener { _, _ -> null  }
        if(checkAccessibilityPermission() && !viewModel.manuallyChanged()) {
            binding.dynamicSwitch.isChecked = true
            binding.demo.isEnabled = true
            binding.ringerDemo.isEnabled = true
            AccessbilityStaticClass.service?.enableView()
        }else if(!checkAccessibilityPermission()){
            binding.dynamicSwitch.isChecked = false
            binding.demo.isEnabled = false
            binding.ringerDemo.isEnabled = false

        }
        binding.dynamicSwitch.setOnCheckedChangeListener { _, b -> setupChangeListener(b) }
        setupNotificationView()
    }

    private fun notificationListenerEnabled(): Boolean {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(this.packageName)
    }

    private fun openNotificationListenerSettings() {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    private fun setupNotificationView() {
        if(notificationListenerEnabled()) {
            binding.notificationCross.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.check))
        }else{
            binding.notificationCross.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.xcross))
        }
    }

    private fun setupBluetoothview() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_CONNECT) != 0) {
            binding.cross.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.xcross))

        } else {
            binding.cross.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.check))
        }

    }

}