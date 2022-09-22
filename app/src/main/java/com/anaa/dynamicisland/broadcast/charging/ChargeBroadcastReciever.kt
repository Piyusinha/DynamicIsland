package com.anaa.dynamicisland.broadcast.charging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager


object ChargeBroadcastReciever {

    private var listener:ChargingListener? = null
    private var statusFinal = BatteryManager.BATTERY_STATUS_NOT_CHARGING

    private var settingReceiver = true


    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val level: Int? = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val status: Int? = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

            if (status == BatteryManager.BATTERY_STATUS_CHARGING && status != statusFinal) {
                statusFinal = status
                if(!settingReceiver) listener?.onChargeConnected(level)
            } else if ((status == BatteryManager.BATTERY_STATUS_DISCHARGING || status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) && status != statusFinal) {
                statusFinal = status
                if(!settingReceiver) listener?.onChargeDisconnected()
            }
            settingReceiver = false
        }
    }

    fun setListener(listener: ChargingListener) {
        this.listener = listener
    }


}
