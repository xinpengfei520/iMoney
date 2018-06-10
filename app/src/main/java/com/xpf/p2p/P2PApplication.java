package com.xpf.p2p;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import com.xpf.common.CommonApplication;
import com.xpf.common.cons.SpKey;
import com.xpf.common.utils.LocaleUtils;
import com.xpf.common.utils.SpUtil;
import com.xpf.http.OklaClient;
import com.xpf.http.logger.XLog;

import java.util.Locale;

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
        changeAppLanguage();
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 设置语言：如果之前有设置就遵循设置如果没设置过就跟随系统语言
     */
    private void changeAppLanguage() {
        int currentLanguage = SpUtil.getInstance(mContext)
                .getInt(SpKey.CURRENT_LANGUAGE, -1);
        Locale myLocale;
        // 0 简体中文 1 繁体中文 2 English
        switch (currentLanguage) {
            case 0:
                myLocale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 1:
                myLocale = Locale.TRADITIONAL_CHINESE;
                break;
            case 2:
                myLocale = Locale.ENGLISH;
                break;
            default:
                myLocale = getResources().getConfiguration().locale;
        }
        // 本地语言设置
        if (LocaleUtils.needUpdateLocale(this, myLocale)) {
            LocaleUtils.updateLocale(this, myLocale);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        XLog.i("onConfigurationChanged");
        setLanguage(newConfig);
    }

    /**
     * 当系统语言发生改变的时候还是继续遵循用户设置的语言
     *
     * @param newConfig
     */
    @SuppressLint("ObsoleteSdkInt")
    private void setLanguage(Configuration newConfig) {
        int currentLanguage = SpUtil.getInstance(mContext)
                .getInt(SpKey.CURRENT_LANGUAGE, -1);
        Locale userLocale;
        // 0 简体中文 1 繁体中文 2 English
        switch (currentLanguage) {
            case 0:
                userLocale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 1:
                userLocale = Locale.TRADITIONAL_CHINESE;
                break;
            case 2:
                userLocale = Locale.ENGLISH;
                break;
            default:
                userLocale = getResources().getConfiguration().locale;
        }
        // 系统语言改变了应用保持之前设置的语言
        if (userLocale != null) {
            Locale.setDefault(userLocale);
            Configuration configuration = new Configuration(newConfig);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(userLocale);
            } else {
                configuration.locale = userLocale;
            }
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }
    }
}
