package com.anaa.dynamicisland.models

import android.graphics.drawable.Drawable
import com.anaa.dynamicisland.NOTCH

data class NotchTypeData(
    var isSelected :Boolean = false,
    var notch: NOTCH,
    var drawable: Drawable?
)