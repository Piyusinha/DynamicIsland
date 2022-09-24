package com.anaa.dynamicisland.accessibilty

import android.accessibilityservice.AccessibilityService
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.anaa.dynamicisland.ui.compose.DynamicIslandComposibleView
import com.anaa.dynamicisland.ui.compose.utils.NotchViewHolder
import com.anaa.dynamicisland.ui.view.DynamicLayoutParams
import com.anaa.dynamicisland.utils.DEFAULT_X
import com.anaa.dynamicisland.view.WindowsViewInflater

class ComposeAccessibiltyService : AccessibilityService() {
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {

    }

    lateinit var windowsViewInflater: WindowsViewInflater

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
        val viewHolder = NotchViewHolder(applicationContext)

        windowsViewInflater = WindowsViewInflater(applicationContext,viewHolder.view)

        viewHolder.view.setContent {
            DynamicIslandComposibleView(viewHolder.viewModel.viewState.value)
        }
        windowsViewInflater.addView(DynamicLayoutParams.getLayoutParams(DEFAULT_X,32, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT))
    }
}