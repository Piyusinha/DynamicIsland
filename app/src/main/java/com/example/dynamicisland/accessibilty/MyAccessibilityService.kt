package com.example.dynamicisland.accessibilty

import android.accessibilityservice.AccessibilityService
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.dynamicisland.R
import com.example.dynamicisland.broadcast.charging.ChargeBroadcastReciever
import com.example.dynamicisland.broadcast.charging.ChargingListener
import com.example.dynamicisland.databinding.ParentLayoutBinding
import com.example.dynamicisland.ui.animation.AnimationListener
import com.example.dynamicisland.ui.animation.TranslationAnimation
import com.example.dynamicisland.ui.view.DynamicLayoutParams
import com.example.dynamicisland.utils.DEFAULT_X
import com.example.dynamicisland.utils.dpToPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class MyAccessibilityService : AccessibilityService() {
    @SuppressLint("ClickableViewAccessibility")
    lateinit var job: Job
    lateinit var scope : CoroutineScope

    private lateinit var rootBinding : ParentLayoutBinding
    private lateinit var windowManager : WindowManager

    private lateinit var defaultLayoutParams : WindowManager.LayoutParams


    override fun onServiceConnected() {
        job = SupervisorJob()
        scope = CoroutineScope(Dispatchers.Main + job)

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val li = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        rootBinding = DataBindingUtil.inflate(li,R.layout.parent_layout,null,false)

        defaultLayoutParams = DynamicLayoutParams.getLayoutParams(DEFAULT_X,32, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)

        try {
            windowManager.addView(rootBinding.root, defaultLayoutParams)

        } catch (ex: Exception) {
            Log.e("ACCSVC", "adding view failed", ex)
        }

        registerReceiver(ChargeBroadcastReciever.broadcastReceiver,IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        ChargeBroadcastReciever.setListener(object :ChargingListener{
            override fun onChargeConnected(level: Int?) {
                setupAnimation(windowManager,rootBinding,dpToPx(236f),defaultLayoutParams.height) { inflateChargeLayout(level, rootBinding) }
            }

            override fun onChargeDisconnected() {
                rootBinding.chargingLayout.root.isVisible = false
                setupAnimation(windowManager,rootBinding,dpToPx(30f),defaultLayoutParams.height) {null}
            }

        })
    }

    private fun setupAnimation(
        windowManager: WindowManager,
        rootBinding: ParentLayoutBinding,
        newWidth: Int,
        height: Int,
        success : () -> Unit?
    ) {
        val ll = DynamicLayoutParams.getLayoutParams(
            DEFAULT_X,32,newWidth,height
        )
        val animation = TranslationAnimation()
        animation.animateViewChange(rootBinding.root.width.toFloat(),ll.width.toFloat(),500L)
        animation.setAnimationListener(object :AnimationListener{
            override fun onAnimate(animator: ValueAnimator) {
                ll.width = (animator.animatedValue as Float).roundToInt()
                windowManager.updateViewLayout(rootBinding.root, ll)
            }

            override fun onAnimateComplete() {
                success()
            }

        })
        animation.start()
    }

    private fun inflateChargeLayout(level: Int?, layout: ParentLayoutBinding) {
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
        scope.launch() {
            delay(2000)
            rootBinding.chargingLayout.root.isVisible = false
            setupAnimation(windowManager,rootBinding,dpToPx(30f),defaultLayoutParams.height) {null}
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

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.i("ACCSVC", "accessibility event $event")
    }

    override fun onInterrupt() {
        Log.i("ACCSVC", "interrupt")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        unregisterReceiver(ChargeBroadcastReciever.broadcastReceiver)
        return super.onUnbind(intent)
    }

}
