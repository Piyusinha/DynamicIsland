package com.anaa.dynamicisland.accessibilty

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.broadcast.charging.ChargeBroadcastReciever
import com.anaa.dynamicisland.broadcast.charging.ChargingListener
import com.anaa.dynamicisland.broadcast.ringer.RingerBroadcastReciever
import com.anaa.dynamicisland.broadcast.ringer.RingerMode
import com.anaa.dynamicisland.databinding.ParentLayoutBinding
import com.anaa.dynamicisland.ui.view.DynamicLayoutParams
import com.anaa.dynamicisland.utils.DEFAULT_X
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MyAccessibilityService : AccessibilityService() {

    val CHANNEL_ID = "ForegroundServiceChannel"

    @SuppressLint("ClickableViewAccessibility")
    lateinit var job: Job
    lateinit var scope : CoroutineScope

    private lateinit var rootBinding : ParentLayoutBinding
    private lateinit var windowManager : WindowManager

    private lateinit var defaultLayoutParams : WindowManager.LayoutParams

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

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
        registerReceiver(RingerBroadcastReciever.receiver,IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION))
        ChargeBroadcastReciever.setListener(object :ChargingListener{
            override fun onChargeConnected(level: Int?) {
                rootBinding.constraintLayout2.setTransition(R.id.charging_transition_start)
                inflateChargeLayout(level, rootBinding)

            }
            override fun onChargeDisconnected() {
                rootBinding.constraintLayout2.transitionToStart()
                rootBinding.chargingLayout.root.isVisible = false
            }
        })

        RingerBroadcastReciever.setListener(object : RingerMode {
            override fun onVibrate() {

            }

            override fun onGeneral() {
                rootBinding.constraintLayout2.setTransition(R.id.ringer_transition_start)
                inflateRinger(ContextCompat.getDrawable(applicationContext,R.drawable.ic_leftnormal),ContextCompat.getColor(applicationContext,R.color.green_dark),"Ring")
                rootBinding.constraintLayout2.transitionToEnd()
                startTimer()
            }

            override fun onSilent() {
                rootBinding.constraintLayout2.setTransition(R.id.ringer_transition_start)
                inflateRinger(ContextCompat.getDrawable(applicationContext,R.drawable.ic_leftsilent_icon),ContextCompat.getColor(applicationContext,R.color.red),"Silent")
                rootBinding.constraintLayout2.transitionToEnd()
                startTimer()
            }

        })
    }

    private fun inflateRinger(drawable: Drawable?, color: Int, s: String) {
        rootBinding.ringerChangeLayout.ringerText.setTextColor(color)
        rootBinding.ringerChangeLayout.ringerText.text = s
        rootBinding.ringerChangeLayout.icon.setImageDrawable(drawable)
    }

    private fun transitionView(view: MotionLayout) {
        when(view.currentState) {
            R.id.charging_end -> view.transitionToState(R.id.charging_start)
        }
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
        scope.launch {
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
