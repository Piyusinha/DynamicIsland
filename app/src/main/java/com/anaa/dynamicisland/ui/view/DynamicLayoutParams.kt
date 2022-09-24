package com.anaa.dynamicisland.ui.view

import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager

object DynamicLayoutParams {
    private const val LAYOUT_FLAG = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
            WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR)


    fun getLayoutParams(x:Int, y:Int, width:Int, height: Int): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
                this.x = x
                this.y = y
                this.width = width
                this.height = height
                type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
                gravity = Gravity.TOP or Gravity.LEFT
                format = PixelFormat.OPAQUE
                flags = LAYOUT_FLAG
        }
        
    }
    fun getActivityLayoutParams(x:Int, y:Int, width:Int, height: Int,gravityVal: Int = Gravity.TOP or Gravity.LEFT): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            this.x = x
            this.y = y
            this.width = width
            this.height = height
            gravity = gravityVal
            format = PixelFormat.TRANSLUCENT

        }

    }
}