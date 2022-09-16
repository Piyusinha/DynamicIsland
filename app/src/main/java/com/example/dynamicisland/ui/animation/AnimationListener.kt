package com.example.dynamicisland.ui.animation

import android.animation.ValueAnimator

interface AnimationListener {
    fun onAnimate(animator: ValueAnimator)
    fun onAnimateComplete()
}