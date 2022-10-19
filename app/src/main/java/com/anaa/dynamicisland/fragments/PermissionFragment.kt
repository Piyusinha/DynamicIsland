package com.anaa.dynamicisland.fragments

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.anaa.dynamicisland.MainActivity
import com.anaa.dynamicisland.MainActivityViewModel
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.databinding.FragmentPermissionBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class PermissionFragment : DaggerFragment() {

    lateinit var binding: FragmentPermissionBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            binding.cross.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.check))
        } else {
            binding.cross.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.xcross))
        }
    }


    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity(),viewModelFactory)[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPermissionBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAccesbilityView()
        setupBluetoothview()
        binding.accesibilty.setOnClickListener {
            if(checkAccessibilityPermission()) {
                return@setOnClickListener
            }
            startDialogFragment()
        }
        binding.confirmPosition.setOnClickListener {
            if(!checkAccessibilityPermission() && ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.BLUETOOTH_CONNECT) != 0) {
                return@setOnClickListener
            }
            sharedViewModel.changeFragment(WelcomeNotchType.newInstance())
        }

        binding.bluetoothPermission.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.BLUETOOTH_CONNECT) == 0) {
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

        val am = activity?.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager?
        am!!.addAccessibilityStateChangeListener { b ->
            if(b) {
                startActivity(Intent(requireActivity(),MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }

    private fun setupNotificationView() {
        if(notificationListenerEnabled()) {
            binding.notificationCross.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.check))
        }else{
            binding.notificationCross.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.xcross))
        }
    }

    private fun setupBluetoothview() {
        if(ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.BLUETOOTH_CONNECT) != 0) {
            binding.cross.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.xcross))

        } else {
            binding.cross.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.check))
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
        }.show(childFragmentManager, SimpleDialog.TAG)
    }

    private fun setupAccesbilityView() {
        if(checkAccessibilityPermission()) {
            binding.crossAccess.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.check))
        } else {
            binding.crossAccess.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.xcross))
        }
    }

    private fun checkAccessibilityPermission(): Boolean {
        var isAccessibilityEnabled = false
        (requireActivity().getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).apply {
            installedAccessibilityServiceList.forEach { installedService ->
                installedService.resolveInfo.serviceInfo.apply {
                    if (getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK).any { it.resolveInfo.serviceInfo.packageName == packageName && it.resolveInfo.serviceInfo.name == name && permission == Manifest.permission.BIND_ACCESSIBILITY_SERVICE && it.resolveInfo.serviceInfo.packageName == requireActivity().packageName })
                        isAccessibilityEnabled = true
                }
            }
        }
        return isAccessibilityEnabled
    }

    private fun notificationListenerEnabled(): Boolean {
        return NotificationManagerCompat.getEnabledListenerPackages(requireActivity()).contains(requireActivity().packageName)
    }

    private fun openNotificationListenerSettings() {
        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
    }

    override fun onResume() {
        super.onResume()
        setupNotificationView()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PermissionFragment()
    }
}