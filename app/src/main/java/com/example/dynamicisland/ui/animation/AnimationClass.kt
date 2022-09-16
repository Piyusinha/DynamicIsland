package com.example.dynamicisland.ui.animation

import android.animation.ValueAnimator
import android.view.WindowManager
import com.example.dynamicisland.databinding.ParentLayoutBinding
import com.example.dynamicisland.ui.view.DynamicLayoutParams
import com.example.dynamicisland.utils.DEFAULT_X
import kotlin.math.roundToInt

object AnimationClass {
    fun setupAnimation(
        windowManager: WindowManager,
        rootBinding: ParentLayoutBinding,
        newWidth: Int,
        height: Int,
        success : () -> Unit?
    ) {
        val ll = DynamicLayoutParams.getLayoutParams(
            DEFAULT_X,32,newWidth,height
        )
        val animation = TranslationAnimation()
        animation.animateViewChange(rootBinding.root.width.toFloat(),ll.width.toFloat(),500L)
        animation.setAnimationListener(object :AnimationListener{
            override fun onAnimate(animator: ValueAnimator) {
                ll.width = (animator.animatedValue as Float).roundToInt()
                windowManager.updateViewLayout(rootBinding.root, ll)
            }

            override fun onAnimateComplete() {
                success()
            }

        })
        animation.start()
    }
}