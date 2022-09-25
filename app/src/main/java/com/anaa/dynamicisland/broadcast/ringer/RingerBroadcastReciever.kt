package com.anaa.dynamicisland.broadcast.ringer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager


object RingerBroadcastReciever {
    private var statusFinal = AudioManager.RINGER_MODE_NORMAL
    private var listener : RingerMode? = null
    var settingReceiver = true


    var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val status: Int? = intent?.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1)
            if(!settingReceiver) {
                when(status) {
                    0 -> listener?.onSilent()
                    1 -> listener?.onVibrate()
                    2 -> listener?.onGeneral()
                }
            }
            settingReceiver = false
        }
    }

    fun setListener(ringerMode: RingerMode){
        this.listener = ringerMode
    }

}