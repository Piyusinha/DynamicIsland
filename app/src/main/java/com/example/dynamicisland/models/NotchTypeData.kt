package com.example.dynamicisland.models

import android.graphics.drawable.Drawable
import com.example.dynamicisland.NOTCH

data class NotchTypeData(
    var isSelected :Boolean = false,
    var notch: NOTCH,
    var drawable: Drawable?
)