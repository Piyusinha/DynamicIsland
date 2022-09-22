package com.anaa.dynamicisland.ui.animation

import android.animation.ValueAnimator
import android.view.WindowManager
import com.anaa.dynamicisland.databinding.ParentLayoutBinding
import com.anaa.dynamicisland.ui.view.DynamicLayoutParams
import com.anaa.dynamicisland.utils.DEFAULT_X
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