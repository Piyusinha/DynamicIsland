package com.example.dynamicisland

import com.example.dynamicisland.utils.DynamixSharedPref

class FragmentRepository(private val sharedPref: DynamixSharedPref) {

    fun setNotchType(type:NOTCH) {
        sharedPref.setNotchType(type)
    }
}