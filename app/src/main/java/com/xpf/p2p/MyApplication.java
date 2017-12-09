package com.xpf.p2p;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by xpf on 2016/11/11 :)
 * Wechat:18091383534
 * Function:代表当前整个应用的实例
 */

public class MyApplication extends Application {

    public static Context mContext;  // 获取全局上下文
    public static Handler mHandler;
    public static Thread mainThread; // 获取主线程
    public static int mainThreadId;  // 获取主线程的id

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        mHandler = new Handler();
        mainThread = Thread.currentThread();       // 当前用于初始化Application的线程，即为主线程
        mainThreadId = android.os.Process.myTid(); // 获取当前主线程的id
        // 设置出现未捕获异常时的处理类
        //CrashHandler.getInstance().init();
    }
}
