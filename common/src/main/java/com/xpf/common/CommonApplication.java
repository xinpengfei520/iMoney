package com.xpf.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.xpf.common.exceptions.GlobalException;

/**
 * Created by xpf on 2018/1/19 :)
 * Function:
 */

public class CommonApplication extends Application {

    /**
     * Global application context.
     */
    @SuppressLint("StaticFieldLeak")
    static Context sContext;
    static Handler mHandler;
    static Thread mainThread; // 获取主线程
    static int mainThreadId = -1;  // 获取主线程的id

    /**
     * Construct of CommonApplication. Initialize application context.
     */
    public CommonApplication() {
        sContext = this;
    }

    /**
     * Use initialize(Context).
     *
     * @param context Application context.
     */
    public static void initialize(Context context) {
        sContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        mainThread = Thread.currentThread(); // 当前用于初始化Application的线程，即为主线程
        mainThreadId = android.os.Process.myTid(); /* 获取当前主线程的id */
        initLogger();
    }

    /**
     * init logger adapter.
     */
    private static void initLogger() {

    }

    /**
     * Get the global application context.
     *
     * @return Application context.
     */
    public static Context getContext() {
        if (sContext == null) {
            throw new GlobalException(GlobalException.APPLICATION_CONTEXT_IS_NULL);
        }
        return sContext;
    }

    public static Handler getHandler() {
        if (mHandler == null) {
            throw new GlobalException(GlobalException.APPLICATION_HANDLER_IS_NULL);
        }
        return mHandler;
    }

    public static Thread getMainThread() {
        if (mainThread == null) {
            throw new GlobalException(GlobalException.APPLICATION_MAINTHREAD_IS_NULL);
        }
        return mainThread;
    }

    public static int getMainThreadId() {
        if (mainThreadId == -1) {
            throw new GlobalException(GlobalException.APPLICATION_MAINTHREADID_IS_NULL);
        }
        return mainThreadId;
    }

}
