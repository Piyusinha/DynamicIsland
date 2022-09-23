package com.anaa.dynamicisland.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.anaa.dynamicisland.NOTCH

class DynamixSharedPref(context: Context?) {
    companion object {
        private const val SHARED_PREF = "DynamicSharedPref"
        private const val SCREEN_SIZE_WIDTH = "screenSizeWidth"
        private const val NOTCH_TYPE = "notch"
        private const val X = "x"
        private const val Y = "y"
        private const val DIMENSION = "dimension"
        private const val RADIUS = "radius"
        private const val SETUP = "setup"
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

    fun setNotchType(type: NOTCH) {
        val prefsEditor = mSharedPref?.edit()
        prefsEditor?.putInt(NOTCH_TYPE, type.value)
        prefsEditor?.apply()
    }
    fun getNotch(): Int? {
        return if(mSharedPref?.contains(NOTCH_TYPE) == true) mSharedPref?.getInt(NOTCH_TYPE,0)
        else null
    }

    fun setXandY(x: Int, y: Int) {
        val prefsEditor = mSharedPref?.edit()
        prefsEditor?.putInt(X, x)
        prefsEditor?.putInt(Y,y)
        prefsEditor?.apply()
    }
    fun getX(): Int {
        return if(mSharedPref?.contains(X) == true) mSharedPref?.getInt(X,0) ?: 0
        else 0
    }

    fun getY(): Int {
        return if(mSharedPref?.contains(Y) == true) mSharedPref?.getInt(Y,0) ?: 0
        else 0
    }

    fun setMinDimension(dimension: Float) {
        val prefsEditor = mSharedPref?.edit()
        prefsEditor?.putFloat(DIMENSION, dimension)
        prefsEditor?.apply()
    }

    fun setRadius(radius: Float) {
        val prefsEditor = mSharedPref?.edit()
        prefsEditor?.putFloat(RADIUS, radius)
        prefsEditor?.apply()
    }
    fun getRadius(): Float {
        return if(mSharedPref?.contains(RADIUS) == true) mSharedPref?.getFloat(RADIUS,0f) ?: 0f
        else 0f
    }
    fun getDimension(): Float {
        return if(mSharedPref?.contains(DIMENSION) == true) mSharedPref?.getFloat(DIMENSION,0f) ?: 0f
        else 0f
    }

    fun setupDone(b: Boolean) {
        val prefsEditor = mSharedPref?.edit()
        prefsEditor?.putBoolean(SETUP, b)
        prefsEditor?.apply()
    }
    fun getSetup(): Boolean {
        return if(mSharedPref?.contains(SETUP) == true) mSharedPref?.getBoolean(SETUP,false) ?: false
        else false
    }

}
