package com.anaa.dynamicisland.ui.activity

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anaa.dynamicisland.AccessbilityStaticClass
import com.anaa.dynamicisland.MainActivity
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.databinding.ActivityIslandBinding
import com.anaa.dynamicisland.databinding.ParentLayoutBinding
import com.anaa.dynamicisland.ui.compose.IslandState
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass
import com.anaa.dynamicisland.ui.view.DynamicLayoutParams
import dagger.android.support.DaggerAppCompatActivity
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIslandBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(viewModel.isSetupDone()) {
            initClickListener()

        }else{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    private fun initClickListener() {
        binding.demo.setOnClickListener {
            AccessbilityStaticClass.service?.updateState(NotchIslandStateSealedClass.ChargingNotch(0,null))
        }
        binding.startAccessbilty.setOnClickListener {
            if(!checkAccessibilityPermission()) {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                return@setOnClickListener
            }
           AccessbilityStaticClass.service?.enableView()
        }
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

    private lateinit var rootBinding : ParentLayoutBinding
    private lateinit var windowsManager : WindowManager

    private lateinit var defaultLayoutParams : WindowManager.LayoutParams

    private fun inflateChargeLayout(level: Int?, layout: ParentLayoutBinding) {
        rootBinding.constraintLayout2.transitionToEnd()
        layout.chargingLayout.root.isVisible = true
        layout.chargingLayout.root.alpha = 0f
        layout.chargingLayout.batteryPercentage.text = "$level%"
        setupBatteryIcon(layout.chargingLayout.battery,level)
        layout.chargingLayout.root.animate()
            .alpha(1.0f)
            .setListener(null)
            .duration = 1000
        startTimer()

    }

    private fun startTimer() {
        lifecycleScope.launch {
            delay(2000)
            rootBinding.constraintLayout2.transitionToStart()
        }
    }

    private fun setupBatteryIcon(battery: ImageView, level: Int?) {
        when(level) {
            in 90..100 -> battery.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_chargepercentage_100))
            in 75..90 -> battery.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_chargepercentage_75))
            in 50..75 -> battery.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_chargepercentage_50))
            in 20..50 -> battery.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_chargepercentage_25))
            in 0..20 -> battery.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_chargepercentage_0))
        }

    }
}