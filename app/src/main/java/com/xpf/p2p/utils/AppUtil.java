package com.xpf.p2p.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
}
