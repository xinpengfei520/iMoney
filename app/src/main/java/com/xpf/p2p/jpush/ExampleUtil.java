package com.xpf.p2p.jpush;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.growingio.android.sdk.utils.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

public class ExampleUtil {

    private static final String TAG = "ExampleUtil";
    private static final Pattern PATTERN_1 = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
    private static final Pattern PATTERN_2 = Pattern.compile("[\\x20-\\x7E]+");
    public static final String PREFS_NAME = "JPUSH_EXAMPLE";
    public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
    public static final String PREFS_START_TIME = "PREFS_START_TIME";
    public static final String PREFS_END_TIME = "PREFS_END_TIME";
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";

    public static boolean isEmpty(String s) {
        if (null == s) {
            return true;
        }
        if (s.length() == 0) {
            return true;
        }
        return s.trim().length() == 0;
    }

    /**
     * 只能以 “+” 或者 数字开头；后面的内容只能包含 “-” 和 数字。
     */
    private final static String MOBILE_NUMBER_CHARS = "^[+0-9][-0-9]{1,}$";

    public static boolean isValidMobileNumber(String s) {
        if (TextUtils.isEmpty(s)) {
            return true;
        }
        Pattern p = Pattern.compile(MOBILE_NUMBER_CHARS);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 校验Tag Alias 只能是数字,英文字母和中文
     *
     * @param s
     * @return
     */
    public static boolean isValidTagAndAlias(String s) {
        Matcher m = PATTERN_1.matcher(s);
        return m.matches();
    }

    /**
     * 获取 AppKey
     *
     * @param context
     * @return
     */
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return appKey;
    }

    // 取得版本号
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (NameNotFoundException e) {
            return "Unknown";
        }
    }

    public static void showToast(final String toast, final Context context) {
        new Thread(() -> {
            Looper.prepare();
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }).start();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String getImei(Context context, String imei) {
        String ret = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            ret = telephonyManager.getDeviceId();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        if (isReadableASCII(ret)) {
            return ret;
        } else {
            return imei;
        }
    }

    private static boolean isReadableASCII(CharSequence string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }

        try {
            return PATTERN_2.matcher(string).matches();
        } catch (Throwable e) {
            return true;
        }
    }

    public static String getDeviceId(Context context) {
        return JPushInterface.getUdid(context);
    }
}
