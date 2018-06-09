package com.xpf.p2p;

import android.app.Application;
import android.content.Context;

import com.xpf.common.CommonApplication;
import com.xpf.http.OklaClient;

/**
 * Created by xpf on 2016/11/11 :)
 * Wechat:18091383534
 * Function:代表当前整个应用的实例
 */

public class P2PApplication extends Application {

    private static Context mContext;  // 获取全局上下文

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        CommonApplication.initialize(this);
        OklaClient.getInstance().init(this);
        // 设置出现未捕获异常时的处理类
        //CrashHandler.getInstance().init();
    }

    public static Context getContext() {
        return mContext;
    }
}
