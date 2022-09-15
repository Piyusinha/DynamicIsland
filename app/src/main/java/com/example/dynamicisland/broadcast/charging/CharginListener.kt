package com.example.dynamicisland.broadcast.charging

interface ChargingListener {
    fun onChargeConnected(level:Int?)
    fun onChargeDisconnected()
}