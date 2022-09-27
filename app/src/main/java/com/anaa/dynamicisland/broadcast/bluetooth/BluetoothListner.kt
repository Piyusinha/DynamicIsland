package com.anaa.dynamicisland.broadcast.bluetooth

import android.bluetooth.BluetoothDevice

interface BluetoothListener {
    fun onDeviceConnected(device: BluetoothDevice?)
}