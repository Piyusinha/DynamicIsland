package com.example.dynamicisland.ui.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator


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
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                listener?.onAnimateComplete()
            }
        })
        valueAnimator.start()
    }

}