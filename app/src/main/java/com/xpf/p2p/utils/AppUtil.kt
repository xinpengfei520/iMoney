package com.xpf.p2p.utils

import android.content.Context
import android.content.pm.PackageManager
import android.view.WindowManager
import com.xpf.p2p.utils.LogUtils

/**
 * Created by xpf on 2017/12/5 :)
 * GitHub:xinpengfei520
 * Function:
 */
object AppUtil {

    @JvmStatic
    fun getVersion(context: Context?): String {
        var version = "unknown"
        try {
            if (context != null) {
                val appContext = context.applicationContext
                val manager = appContext.packageManager
                val packageInfo = manager.getPackageInfo(appContext.packageName, 0)
                version = packageInfo.versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    @JvmStatic
    fun getAppVersionCode(context: Context): Int {
        var versioncode = 0
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            val versionName = pi.versionName
            versioncode = pi.versionCode
            if (versionName == null || versionName.isEmpty()) {
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.e("AppUtil", "Exception$e")
        }
        return versioncode
    }

    @JvmStatic
    fun getAppVersionName(ctx: Context): String {
        var localVersion = ""
        try {
            val packageInfo = ctx.applicationContext
                .packageManager
                .getPackageInfo(ctx.packageName, 0)
            localVersion = packageInfo.versionName
            LogUtils.d("AppUtil", "本软件的版本号。。$localVersion")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return localVersion
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    fun getScreenDisplay(context: Context): IntArray {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = windowManager.defaultDisplay.width
        val height = windowManager.defaultDisplay.height
        return intArrayOf(width, height)
    }
}
