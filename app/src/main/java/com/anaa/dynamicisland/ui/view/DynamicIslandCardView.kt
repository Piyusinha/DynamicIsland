package com.anaa.dynamicisland.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.WindowManager
import androidx.cardview.widget.CardView


class DynamicIslandCardView : CardView {

    private var viewContext : Context

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        this.viewContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {
        this.viewContext = context
    }
    constructor(context: Context,layoutParams : WindowManager.LayoutParams) : super(context) {
        this.viewContext = context
    }

    constructor(context: Context, radius:Float) : super(context) {
        this.viewContext = context
        this.setCardBackgroundColor(Color.BLACK)
        this.radius = radius
    }

}
