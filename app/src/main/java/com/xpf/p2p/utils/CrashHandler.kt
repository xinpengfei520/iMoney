package com.xpf.p2p.utils

import android.os.Build
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Toast

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:vancexin
 * Function:提供一个出现未被捕获异常时,显式捕获的类(单例)
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    var defaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

    fun init() {
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        Thread {
            Looper.prepare()
            Toast.makeText(UIUtils.getContext(), "亲,出现异常了!店小二正在努力修复", Toast.LENGTH_SHORT).show()
            Looper.loop()
        }.start()

        collectionException(e)
        SystemClock.sleep(2000)
        ActivityManager.instance.removeAll()
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    private fun collectionException(e: Throwable) {
        val exception = e.message
        val message = "${Build.DEVICE}:${Build.MODEL}:${Build.PRODUCT}:${Build.VERSION.SDK_INT}"
        Thread {
            Log.e("TAG", "exception = $exception,message = $message")
        }.start()
    }

    companion object {
        @JvmStatic
        val instance: CrashHandler by lazy { CrashHandler() }
    }
}
