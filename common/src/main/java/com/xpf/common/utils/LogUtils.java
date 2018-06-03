package com.xpf.common.utils;

import android.util.Log;

/**
 * Created by xpf on 2017/9/22 :)
 * Function: logger utils.
 */

public class LogUtils {

    private static boolean mDebug = true;// default open log print.

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

    public static void eLogging(String TAG, String message) {
        if (mDebug) {
            com.orhanobut.logger.Logger.t(TAG).e(message);
        }
    }


    public static void iLogging(String TAG, String message) {
        if (mDebug) {
            com.orhanobut.logger.Logger.t(TAG).i(message);
        }
    }

    public static void dLogging(String TAG, String message) {
        if (mDebug) {
            com.orhanobut.logger.Logger.t(TAG).d(message);
        }
    }

    public static void vLogging(String TAG, String message) {
        if (mDebug) {
            com.orhanobut.logger.Logger.t(TAG).v(message);
        }
    }

    public static void wLogging(String TAG, String message) {
        if (mDebug) {
            com.orhanobut.logger.Logger.t(TAG).w(message);
        }
    }

    public static void wtfLogging(String TAG, String message) {
        if (mDebug) {
            com.orhanobut.logger.Logger.t(TAG).wtf(message);
        }
    }

    public static void json(String TAG, String json) {
        if (mDebug) {
            com.orhanobut.logger.Logger.t(TAG).json(json);
        }
    }
}
