package com.xpf.p2p.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * 沉浸式状态栏工具类
 */
object StatusBarUtils {

    /**
     * 设置沉浸式状态栏（状态栏透明，内容延伸到状态栏下方）
     * @param activity Activity
     * @param darkIcons 是否使用深色状态栏图标（浅色背景用 true，深色背景用 false）
     */
    @JvmStatic
    fun setImmersiveStatusBar(activity: Activity, darkIcons: Boolean = false) {
        val window = activity.window
        // 内容延伸到状态栏
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 状态栏透明
        window.statusBarColor = Color.TRANSPARENT
        // 设置状态栏图标颜色
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = darkIcons
    }

    /**
     * 获取状态栏高度（px）
     */
    @JvmStatic
    fun getStatusBarHeight(activity: Activity): Int {
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) activity.resources.getDimensionPixelSize(resourceId) else 0
    }
}
