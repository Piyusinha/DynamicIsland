package com.example.dynamicisland.broadcast.ringer

interface RingerMode {
    fun onVibrate()
    fun onGeneral()
    fun onSilent()
}