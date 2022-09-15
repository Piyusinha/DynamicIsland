package com.example.dynamicisland.accessibilty

import android.accessibilityservice.AccessibilityService
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.databinding.DataBindingUtil
import com.example.dynamicisland.R
import com.example.dynamicisland.broadcast.charging.ChargeBroadcastReciever
import com.example.dynamicisland.broadcast.charging.ChargingListener
import com.example.dynamicisland.databinding.ParentLayoutBinding
import com.example.dynamicisland.ui.animation.AnimationListener
import com.example.dynamicisland.ui.animation.TranslationAnimation
import com.example.dynamicisland.ui.view.DynamicLayoutParams
import com.example.dynamicisland.utils.DynamixSharedPref
import kotlin.math.roundToInt


class MyAccessibilityService : AccessibilityService() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onServiceConnected() {

        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val li = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootBinding = DataBindingUtil.inflate<ParentLayoutBinding>(li,R.layout.parent_layout,null,false)

        val width = DynamixSharedPref(applicationContext).getWidth()
        val layoutParams = DynamicLayoutParams.getLayoutParams(40,32, WindowManager.LayoutParams.WRAP_CONTENT,70)

        try {
            windowManager.addView(rootBinding.root, layoutParams)

        } catch (ex: Exception) {
            Log.e("ACCSVC", "adding view failed", ex)
        }

        rootBinding.root.setOnTouchListener { _, _ ->
            val ll = DynamicLayoutParams.getLayoutParams(
                40,32,width?.minus(80) ?: 600,layoutParams.height
            )
            val animation = TranslationAnimation()
            animation.animateViewChange(rootBinding.root.width.toFloat(),ll.width.toFloat(),500L)
            animation.setAnimationListener(object :AnimationListener{
                override fun onAnimate(animator: ValueAnimator) {
                    ll.width = (animator.animatedValue as Float).roundToInt()
                    if(ll.width/4 > layoutParams.height) ll.height = ll.width/4
                    windowManager.updateViewLayout(rootBinding.root, ll)
                }

            })
            animation.start()
            true
        }
        registerReceiver(ChargeBroadcastReciever.broadcastReceiver,IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        ChargeBroadcastReciever.setListener(object :ChargingListener{
            override fun onChargeConnected(level: Int?) {
                inflateChargeLayout(level,rootBinding.root)
            }

            override fun onChargeDisconnected() {
                TODO("Not yet implemented")
            }

        })
    }

    private fun inflateChargeLayout(level: Int?, layout: View?) {
        val li = getSystemService(LAYOUT_INFLATER_SERVICE) as? LayoutInflater
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
