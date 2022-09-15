package com.example.dynamicisland.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class DynamixSharedPref(context: Context?) {
    companion object {
        private const val SHARED_PREF = "DynamicSharedPref"
        private const val SCREEN_SIZE_WIDTH = "screenSizeWidth"
    }
    private var mSharedPref: SharedPreferences? = context?.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE)

    fun setWidth(width:Int) {
        val prefsEditor = mSharedPref?.edit()
        prefsEditor?.putInt(SCREEN_SIZE_WIDTH, width)
        prefsEditor?.apply()
    }

    fun getWidth(): Int? {
        return if(mSharedPref?.contains(SCREEN_SIZE_WIDTH) == true) mSharedPref?.getInt(SCREEN_SIZE_WIDTH,600)
        else null
    }
}
