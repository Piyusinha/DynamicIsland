package com.anaa.dynamicisland.broadcast.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


object LocalBroadcast  {
    private const val ENABLE = "enable"
    private var listener : LocalBroadcastInterfaceListner? = null
    private class LocalBroadcastReciever : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == ENABLE) {
                listener?.enableView()
            }
        }
    }

    fun setListener(ringerMode: LocalBroadcastInterfaceListner){
        this.listener = ringerMode
    }


}

interface LocalBroadcastInterfaceListner {
    fun enableView()
}
