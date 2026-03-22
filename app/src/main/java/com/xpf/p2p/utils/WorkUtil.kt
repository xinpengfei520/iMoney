package com.xpf.p2p.utils

import android.content.Context
import android.util.Log

/**
 * Created by xpf on 2017/8/26 :)
 * Function:
 */
object WorkUtil {

    private val TAG = WorkUtil::class.java.simpleName

    @Suppress("DEPRECATION")
    @JvmStatic
    fun isServiceWorked(context: Context, serviceName: String): Boolean {
        val myManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val runningService = myManager.getRunningServices(Int.MAX_VALUE) as ArrayList<android.app.ActivityManager.RunningServiceInfo>
        Log.e(TAG, "当前运行的: $runningService")
        for (i in runningService.indices) {
            if (runningService[i].service.className == serviceName) {
                return true
            }
        }
        return false
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    fun isRun(context: Context?): Boolean {
        if (context == null) return false
        val appContext = context.applicationContext
        val am = appContext.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val list = am.getRunningTasks(100)
        var isAppRunning = false
        val MY_PKG_NAME = context.packageName
        for (info in list) {
            if (info.topActivity!!.packageName == MY_PKG_NAME || info.baseActivity!!.packageName == MY_PKG_NAME) {
                isAppRunning = true
                Log.i(TAG, "${info.topActivity!!.packageName} info.baseActivity.getPackageName()=${info.baseActivity!!.packageName}")
                break
            }
        }
        Log.i(TAG, "程序 isAppRunning......$isAppRunning")
        return isAppRunning
    }
}
