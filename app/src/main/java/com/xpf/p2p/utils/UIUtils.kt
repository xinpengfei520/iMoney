package com.xpf.p2p.utils

import android.content.Context
import android.os.Handler
import android.view.View
import com.xpf.p2p.App

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:vancexin
 * Function:获取ui操作的相关工具类
 */
object UIUtils {

    @JvmStatic
    fun getContext(): Context {
        return App.context
    }

    @JvmStatic
    fun getHandler(): Handler {
        return App.mHandler!!
    }

    @JvmStatic
    fun getColor(colorId: Int): Int {
        @Suppress("DEPRECATION")
        return getContext().resources.getColor(colorId)
    }

    @JvmStatic
    fun getView(layoutId: Int): View {
        return View.inflate(getContext(), layoutId, null)
    }

    @JvmStatic
    fun dp2px(dp: Int): Int {
        val density = getContext().resources.displayMetrics.density
        return (density * dp + 0.5).toInt()
    }

    @JvmStatic
    fun px2dp(px: Int): Int {
        val density = getContext().resources.displayMetrics.density
        return (px / density + 0.5).toInt()
    }

    @JvmStatic
    fun getStrArray(strArrayId: Int): Array<String> {
        return getContext().resources.getStringArray(strArrayId)
    }

    @JvmStatic
    fun runOnUiThread(runnable: Runnable) {
        if (isMainThread()) {
            runnable.run()
        } else {
            getHandler().post(runnable)
        }
    }

    private fun isMainThread(): Boolean {
        val currentThreadId = android.os.Process.myTid()
        return App.mainThreadId == currentThreadId
    }
}
