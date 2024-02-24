package com.xpf.p2p;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.growingio.android.sdk.collection.GrowingIO;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.xpf.common.CommonApplication;
import com.xpf.common.cons.SpKey;
import com.xpf.common.utils.LocaleUtils;
import com.xpf.common.utils.SpUtil;
import com.xpf.http.OklaClient;
import com.xpf.http.logger.XLog;
import com.xpf.p2p.uetool.FilterOutView;

import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import me.ele.uetool.UETool;

/**
 * Created by xpf on 2016/11/11 :)
 * Function:代表当前整个应用的实例
 */

public class P2PApplication extends MultiDexApplication {

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

        // 初始化MobSDK
        MobSDK.init(this, "266ce6392d6fe", "6d4da648f3c2eef26eb682641d414c1c");

        /*
         * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
         * 输出详细的Bugly SDK的Log；
         * 每一条Crash都会被立即上报；
         * 自定义日志将会在Logcat中输出。
         * 建议在测试阶段建议设置成true，发布时设置为false。
         */
        CrashReport.initCrashReport(getApplicationContext(), "c84e7e9ad7", BuildConfig.DEBUG);

        initUETool();

        initGrowingIO();

        initJPushSdk();
    }

    private void initJPushSdk() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
    }

    private void initGrowingIO() {
        GrowingIO.startWithConfiguration(this,
                new com.growingio.android.sdk.collection.Configuration()
                        .trackAllFragments()
                        .setTestMode(BuildConfig.DEBUG)
                        .setDebugMode(BuildConfig.DEBUG)
                        .setChannel("pgyer")
        );
    }

    private void initUETool() {
        UETool.putFilterClass(FilterOutView.class);
        UETool.putAttrsProviderClass(CustomAttribution.class);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            private int visibleActivityCount;
            private int uetoolDismissY = -1;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                visibleActivityCount++;
                if (visibleActivityCount == 1 && uetoolDismissY >= 0) {
                    UETool.showUETMenu(uetoolDismissY);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                visibleActivityCount--;
                if (visibleActivityCount == 0) {
                    uetoolDismissY = UETool.dismissUETMenu();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
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
