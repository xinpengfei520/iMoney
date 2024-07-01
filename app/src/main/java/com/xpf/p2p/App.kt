package com.xpf.p2p

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import androidx.multidex.MultiDexApplication
import cn.jpush.android.api.JPushInterface
import com.growingio.android.sdk.collection.GrowingIO
import com.mob.MobSDK
import com.tencent.bugly.crashreport.CrashReport
import com.xpf.http.OklaClient
import com.xpf.http.logger.XLog
import com.xpf.p2p.constants.SpKey
import com.xpf.p2p.uetool.FilterOutView
import com.xpf.p2p.utils.LocaleUtils
import com.xpf.p2p.utils.SpUtil
import me.ele.uetool.UETool
import java.util.Locale

/**
 * Created by xpf on 2016/11/11 :)
 * Function:代表当前整个应用的实例
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        mHandler = Handler(Looper.getMainLooper())
        mainThread = Thread.currentThread() // 当前用于初始化Application的线程，即为主线程
        mainThreadId = Process.myTid() /* 获取当前主线程的id */

        OklaClient.getInstance().init(this)
        // 设置出现未捕获异常时的处理类
        //CrashHandler.getInstance().init();
        changeAppLanguage()

        // 初始化MobSDK
        MobSDK.init(this, "266ce6392d6fe", "6d4da648f3c2eef26eb682641d414c1c")

        /*
         * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
         * 输出详细的Bugly SDK的Log；
         * 每一条Crash都会被立即上报；
         * 自定义日志将会在Logcat中输出。
         * 建议在测试阶段建议设置成true，发布时设置为false。
         */
        CrashReport.initCrashReport(applicationContext, "c84e7e9ad7", BuildConfig.DEBUG)

        initUETool()
        initGrowingIO()
        initJPushSdk()
    }

    private fun initJPushSdk() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(this)
    }

    private fun initGrowingIO() {
        GrowingIO.startWithConfiguration(
            this,
            com.growingio.android.sdk.collection.Configuration()
                .trackAllFragments()
                .setTestMode(BuildConfig.DEBUG)
                .setDebugMode(BuildConfig.DEBUG)
                .setChannel("pgyer")
        )
    }

    private fun initUETool() {
        UETool.putFilterClass(FilterOutView::class.java)
        UETool.putAttrsProviderClass(CustomAttribution::class.java)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var visibleActivityCount = 0
            private var uetoolDismissY = -1

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
                visibleActivityCount++
                if (visibleActivityCount == 1 && uetoolDismissY >= 0) {
                    UETool.showUETMenu(uetoolDismissY)
                }
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                visibleActivityCount--
                if (visibleActivityCount == 0) {
                    uetoolDismissY = UETool.dismissUETMenu()
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }

    /**
     * 设置语言：如果之前有设置就遵循设置如果没设置过就跟随系统语言
     */
    private fun changeAppLanguage() {
        val currentLanguage = SpUtil.getInstance(context)
            .getInt(SpKey.CURRENT_LANGUAGE, -1)
        val myLocale = when (currentLanguage) {
            0 -> Locale.SIMPLIFIED_CHINESE
            1 -> Locale.TRADITIONAL_CHINESE
            2 -> Locale.ENGLISH
            else -> resources.configuration.locales.get(0)
        }
        // 本地语言设置
        if (LocaleUtils.needUpdateLocale(this, myLocale)) {
            LocaleUtils.updateLocale(this, myLocale)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        XLog.i("onConfigurationChanged")
        setLanguage(newConfig)
    }

    /**
     * 当系统语言发生改变的时候还是继续遵循用户设置的语言
     *
     * @param newConfig
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun setLanguage(newConfig: Configuration) {
        val currentLanguage = SpUtil.getInstance(context)
            .getInt(SpKey.CURRENT_LANGUAGE, -1)
        val userLocale = when (currentLanguage) {
            0 -> Locale.SIMPLIFIED_CHINESE
            1 -> Locale.TRADITIONAL_CHINESE
            2 -> Locale.ENGLISH
            else -> resources.configuration.locales.get(0)
        }
        // 系统语言改变了应用保持之前设置的语言
        Locale.setDefault(userLocale)
        val configuration = Configuration(newConfig)
        configuration.setLocale(userLocale)
        //resources.updateConfiguration(configuration, resources.displayMetrics)
        createConfigurationContext(configuration)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var mHandler: Handler? = null
        var mainThread: Thread? = null // 获取主线程
        var mainThreadId: Int = -1 // 获取主线程的id
    }
}
