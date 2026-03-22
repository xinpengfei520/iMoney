package com.xpf.p2p.utils

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

/**
 * DrawUtils绘制矩形的工具类
 */
object DrawUtils {

    @JvmStatic
    fun getDrawable(rgb: Int, radius: Float): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(rgb)
        gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
        gradientDrawable.cornerRadius = radius
        gradientDrawable.setStroke(UIUtils.dp2px(1), rgb)
        return gradientDrawable
    }

    @JvmStatic
    fun getSelector(normalDrawable: Drawable, pressDrawable: Drawable): StateListDrawable {
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed), pressDrawable)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), normalDrawable)
        stateListDrawable.addState(intArrayOf(), normalDrawable)
        return stateListDrawable
    }
}
