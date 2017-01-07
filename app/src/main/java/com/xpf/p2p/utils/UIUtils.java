package com.xpf.p2p.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.xpf.p2p.MyApplication;

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:18091383534
 * Function:获取ui操作的相关工具类
 */

public class UIUtils {

    // 获取程序需要的上下文对象:返回的是MyApplication的对象
    public static Context getContext() {
        return MyApplication.mContext;
    }

    // 获取程序需要的消息处理器的对象
    public static Handler getHandler() {
        return MyApplication.mHandler;
    }

    // 返回指定id对应的颜色
    public static int getColor(int colorId) {
        return getContext().getResources().getColor(colorId);
    }

    // 返回指定布局id,所对应的视图对象
    public static View getView(int layoutId) {
        View view = View.inflate(getContext(), layoutId, null);
        return view;
    }

    // dp--->px
    public static int dp2px(int dp) {
        // 获取手机的密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5); // 实现四舍五入操作
    }

    // px--->dp
    public static int px2dp(int px) {
        // 获取手机的密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5); // 实现四舍五入操作
    }

    // 返回指定id的字符串数组
    public static String[] getStrArray(int strArrayId) {
        String[] stringArray = getContext().getResources().getStringArray(strArrayId);
        return stringArray;
    }

    // 保证如下的操作在主线程中执行的
    public static void runOnUiThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            UIUtils.getHandler().post(runnable);
        }
    }

    // 判断当前的线程是否是主线程
    private static boolean isMainThread() {
        int currentThreadId = android.os.Process.myTid();
        return MyApplication.mainThreadId == currentThreadId;
    }
}
