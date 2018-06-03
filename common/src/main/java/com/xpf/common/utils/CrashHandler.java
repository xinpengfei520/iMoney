package com.xpf.common.utils;

import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.xpf.common.manager.ActivityManager;

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:18091383534
 * Function:提供一个出现未被捕获异常时,显式捕获的类(单例)
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    // 懒汉式:私有化构造器
    private CrashHandler() {
    }

    private static CrashHandler instance = null;

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init() {
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前的类,作为出现未捕获异常时的处理类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    // 当程序执行过程中,一旦出现未被捕获的异常时,即调用如下的回调方法
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // Log.e("TAG", "出现异常了！异常信息为：" + ex.getMessage());
        // 处理异常的操作,系统单独的提供了一个分线程来完成
        new Thread() {
            @Override
            public void run() {
                // android系统中,默认情况下，线程是不可以开启Looper进行消息的处理的，除非是主线程
                Looper.prepare();
                // Toast的操作属于更新界面的操作,必须放在主线程中执行
                Toast.makeText(UIUtils.getContext(), "亲,出现异常了!店小二正在努力修复", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        // 收集用户出现的异常信息,并发送给后台
        collectionException(e);

        SystemClock.sleep(2000);

        // 移除栈空间中所有的activity
        ActivityManager.getInstance().removeAll();
        // 结束当前的进程
        android.os.Process.killProcess(android.os.Process.myPid());
        // 结束当前的虚拟机的执行
        System.exit(0);
    }

    private void collectionException(Throwable e) {

        final String exception = e.getMessage();
        // 手机用户手机的设备信息
        final String message = Build.DEVICE + ":" + Build.MODEL + ":" + Build.PRODUCT + ":" + Build.VERSION.SDK_INT;
        // 模拟联网操作
        new Thread() {
            @Override
            public void run() {
                Log.e("TAG", "exception = " + exception + ",message = " + message);
            }
        }.start();
    }
}
