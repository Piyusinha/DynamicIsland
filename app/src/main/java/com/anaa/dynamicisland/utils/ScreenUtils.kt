package com.anaa.dynamicisland.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.roundToInt

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
fun Context.dpToPx(dpValue: Float): Int {
    val displayMetrics = resources.displayMetrics
    return (dpValue * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}
fun Context.pxTodp(pxValue: Float): Int {
    val displayMetrics = resources.displayMetrics
    return (pxValue / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()

}
