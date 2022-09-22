package com.anaa.dynamicisland.view

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.view.View
import android.view.WindowManager
import java.lang.Exception

class WindowsViewInflater(private val context: Context?, private val view:View) {

   private val windowManager = context?.getSystemService(WINDOW_SERVICE) as WindowManager

    fun addView(params: WindowManager.LayoutParams?) {
        try {
            windowManager.addView(view, params)
        }catch (e:Exception) {

        }
    }

    fun updateView(params: WindowManager.LayoutParams?) {
        try {
            windowManager.updateViewLayout(view,params)
        }catch (e:Exception) {

        }
    }

    fun removeView() {
        try {
            windowManager.removeView(view)
        }catch (e:Exception) {

        }

    }
}