package com.anaa.dynamicisland.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.WindowManager
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
import com.anaa.dynamicisland.MainActivity
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.databinding.ActivityIslandBinding
import com.anaa.dynamicisland.databinding.ParentLayoutBinding
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
            createDemoView()
            initClickListener()

        }else{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    private fun initClickListener() {
        binding.demo.setOnClickListener {
            startAnimation()
        }
        binding.startAccessbilty.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    private lateinit var rootBinding : ParentLayoutBinding
    private lateinit var windowsManager : WindowManager

    private lateinit var defaultLayoutParams : WindowManager.LayoutParams


    private fun createDemoView() {
       windowsManager = getSystemService(WINDOW_SERVICE) as WindowManager
       val li = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
      rootBinding = DataBindingUtil.inflate(li,R.layout.parent_layout,null,false)
       defaultLayoutParams = DynamicLayoutParams.getActivityLayoutParams(viewModel.getX(),viewModel.getY(), WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,viewModel.getGravity())
        windowsManager.addView(rootBinding.root,defaultLayoutParams);
        setupCardViewSize()
    }

    private fun setupCardViewSize() {
        rootBinding.root.minimumWidth = viewModel.getDimension().toInt()
        rootBinding.root.minimumHeight = viewModel.getDimension().toInt()
        rootBinding.constraintLayout2.minimumWidth = viewModel.getDimension().toInt()
        rootBinding.constraintLayout2.minimumHeight = viewModel.getDimension().toInt()
        (rootBinding.root as? CardView)?.radius = viewModel.getRadius()
    }

    private fun startAnimation() {
        rootBinding.constraintLayout2.setTransition(R.id.charging_transition_start)
        inflateChargeLayout(75, rootBinding)
    }
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