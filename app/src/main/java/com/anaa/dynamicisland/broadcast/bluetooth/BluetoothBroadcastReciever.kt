package com.anaa.dynamicisland.broadcast.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


object BluetoothBroadcastReciever {

    private var listener: BluetoothListener? = null

    var settingReceiver = true

    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            Log.d("Piyush-device",intent?.action.toString())
            val action = intent?.action
            val device: BluetoothDevice? = intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

            if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
                listener?.onDeviceConnected(device)
            }

        }
    }
    fun setListener(listener: BluetoothListener) {
        this.listener = listener
    }

}