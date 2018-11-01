package com.xpf.p2p.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.xpf.http.logger.XLog;

/**
 * Created by xpf on 2017/12/5 :)
 * GitHub:xinpengfei520
 * Function:
 */
public class AppUtil {

    /**
     * 获取当前应用的版本号
     * 如果找不到对应的应用包信息, 就返回"未知版本":unknown
     *
     * @param context 上下文
     * @return 版本号
     */
    public static String getVersion(Context context) {
        String version = "unknown";
        try {
            if (context != null) {
                Context appContext = context.getApplicationContext();
                PackageManager manager = appContext.getPackageManager();
                PackageInfo packageInfo = manager.getPackageInfo(appContext.getPackageName(), 0);
                version = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        String versionName = "";
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            XLog.e("Exception" + e);
        }
        return versioncode;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getAppVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            XLog.d("本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
