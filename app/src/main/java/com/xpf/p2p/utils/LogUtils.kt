package com.xpf.p2p.utils

import android.util.Log
import com.xpf.p2p.BuildConfig

/**
 * Created by xpf on 2017/9/22 :)
 * Function: logger utils.
 */
object LogUtils {

    private var mDebug = BuildConfig.ENABLE_DEBUG

    @JvmStatic
    fun isDebug(isDebug: Boolean) {
        mDebug = isDebug
    }

    @JvmStatic
    fun e(TAG: String, message: String?) {
        if (mDebug) {
            Log.e(TAG, message ?: "null")
        }
    }

    @JvmStatic
    fun i(TAG: String, message: String?) {
        if (mDebug) {
            Log.i(TAG, message ?: "null")
        }
    }

    @JvmStatic
    fun d(TAG: String, message: String?) {
        if (mDebug) {
            Log.d(TAG, message ?: "null")
        }
    }

    @JvmStatic
    fun v(TAG: String, message: String?) {
        if (mDebug) {
            Log.v(TAG, message ?: "null")
        }
    }

    @JvmStatic
    fun w(TAG: String, message: String?) {
        if (mDebug) {
            Log.w(TAG, message ?: "null")
        }
    }

    @JvmStatic
    fun wtf(TAG: String, message: String?) {
        if (mDebug) {
            Log.wtf(TAG, message ?: "null")
        }
    }
}
