package com.example.dynamicisland.ui.animation

import android.animation.ValueAnimator
import java.time.Duration

class TranslationAnimation() {

    val valueAnimator = ValueAnimator.ofFloat()
    var listener :AnimationListener? = null

    fun animateViewChange(start:Float,end:Float,duration: Long) {
        valueAnimator.setFloatValues(start,end)
        valueAnimator.duration = duration
    }

    fun setAnimationListener(listener :AnimationListener) {
        this.listener = listener
    }

    fun start() {
        valueAnimator.addUpdateListener {
            listener?.onAnimate(it)
        }
        valueAnimator.start()
    }

}