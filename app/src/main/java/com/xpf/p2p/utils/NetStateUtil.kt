package com.xpf.p2p.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by xpf on 2017/12/5 :)
 * GitHub:xinpengfei520
 * Function:检测网络状态的工具类
 */
object NetStateUtil {

    @JvmStatic
    fun isConnected(context: Context?): Boolean {
        if (context != null) {
            val appContext = context.applicationContext
            val manager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (manager != null) {
                @Suppress("DEPRECATION")
                val networkInfo = manager.activeNetworkInfo
                @Suppress("DEPRECATION")
                return networkInfo != null && networkInfo.isConnected
            }
            return false
        } else {
            return false
        }
    }
}
