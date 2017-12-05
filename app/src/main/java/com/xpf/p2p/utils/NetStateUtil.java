package com.xpf.p2p.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xpf on 2017/12/5 :)
 * GitHub:xinpengfei520
 * Function:检测网络状态的工具类
 */

public class NetStateUtil {

    /**
     * 判断手机是否可以联网
     */
    public static boolean isConnected(Context context) {
        if (context != null) {
            Context appContext = context.getApplicationContext();
            ConnectivityManager manager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                return (networkInfo != null) && (networkInfo.isConnected());
            }
            return false;
        } else {
            return false;
        }
    }
}
