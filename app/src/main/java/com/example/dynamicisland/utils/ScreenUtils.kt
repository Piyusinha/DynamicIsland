package com.example.dynamicisland.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

class ScreenUtils {

    fun getScreenWidth(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()

        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getScreenHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}