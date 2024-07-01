package com.xpf.common.utils;

import android.util.Log;

import com.xpf.common.BuildConfig;

/**
 * Created by xpf on 2017/9/22 :)
 * Function: logger utils.
 */

public class LogUtils {

    // default open log print.
    private static boolean mDebug = BuildConfig.ENABLE_DEBUG;

    private LogUtils() {
    }

    public static void isDebug(boolean isDebug) {
        mDebug = isDebug;
    }

    public static void e(String TAG, String message) {
        if (mDebug) {
            Log.e(TAG, message);
        }
    }


    public static void i(String TAG, String message) {
        if (mDebug) {
            Log.i(TAG, message);
        }
    }

    public static void d(String TAG, String message) {
        if (mDebug) {
            Log.d(TAG, message);
        }
    }

    public static void v(String TAG, String message) {
        if (mDebug) {
            Log.v(TAG, message);
        }
    }

    public static void w(String TAG, String message) {
        if (mDebug) {
            Log.w(TAG, message);
        }
    }

    public static void wtf(String TAG, String message) {
        if (mDebug) {
            Log.wtf(TAG, message);
        }
    }
}
