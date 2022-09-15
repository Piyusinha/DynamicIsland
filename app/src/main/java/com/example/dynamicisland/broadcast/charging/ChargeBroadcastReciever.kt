package com.example.dynamicisland.broadcast.charging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.widget.Toast


object ChargeBroadcastReciever {

    private var listener:ChargingListener? = null
    private var statusFinal = BatteryManager.BATTERY_STATUS_NOT_CHARGING


    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val level: Int? = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val status: Int? = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

            if (status == BatteryManager.BATTERY_STATUS_CHARGING && status != statusFinal) {
               Toast.makeText(p0,"Charging",Toast.LENGTH_SHORT).show()
                listener?.onChargeConnected(level)
            } else if ((status == BatteryManager.BATTERY_STATUS_DISCHARGING || status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) && status != statusFinal) {
                Toast.makeText(p0,"Discharging",Toast.LENGTH_SHORT).show()
                listener?.onChargeDisconnected()
            }
        }
    }

    fun setListener(listener: ChargingListener) {
        this.listener = listener
    }


}
