package com.example.dynamicisland

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.dynamicisland.di.custom_annotation.ApplicationContext
import com.example.dynamicisland.models.NotchTypeData
import com.example.dynamicisland.utils.DynamixSharedPref

class FragmentRepository(private val sharedPref: DynamixSharedPref,private val context: Context) {

    fun setNotchType(type:NOTCH) {
        sharedPref.setNotchType(type)
    }

    fun getNotch(): Int? {
        return sharedPref.getNotch()
    }

    fun setXandY(x:Int,y:Int) {
        sharedPref.setXandY(x,y)
    }

    fun getNotchList(): MutableList<Any> {
        val mutableList = mutableListOf<Any>()
        NOTCH.values().forEach {
            mutableList.add(NotchTypeData(false,it,getDrawableFromType(it)))
        }
        return mutableList
    }

    private fun getDrawableFromType(it: NOTCH): Drawable? {
        when(it.value) {
            0 -> return ContextCompat.getDrawable(context,R.drawable.ic_punch_hole_left)
            1 -> return ContextCompat.getDrawable(context,R.drawable.ic_punch_hole_right)
            2 -> return ContextCompat.getDrawable(context,R.drawable.ic_punch_hole_center)
            3 -> return ContextCompat.getDrawable(context,R.drawable.ic_center_notch)
        }
        return null
    }
}