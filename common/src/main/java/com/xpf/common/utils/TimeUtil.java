package com.xpf.common.utils;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.xpf.common.CommonApplication;
import com.xpf.common.cons.SpKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xpf on 2017/4/15 :)
 * Function:获取时间的工具类
 */
public class TimeUtil {

    private static final String TAG = TimeUtil.class.getSimpleName();
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * 返回如下格式的时间戳
     *
     * @return 20170311-11:00:12
     */
    public static String getCurrentTime() {
        CharSequence sysTimeStr = DateFormat.format("yyyyMMdd-HH:mm:ss", System.currentTimeMillis());
        return String.valueOf(sysTimeStr);
    }

    /**
     * 返回如下格式的时间戳
     *
     * @return 20170311
     */
    public static String getCurrentDay() {
        CharSequence sysTimeStr = DateFormat.format("yyyyMMdd", System.currentTimeMillis());
        return String.valueOf(sysTimeStr);
    }

    /**
     * 返回如下格式的时间戳
     *
     * @return 2017-03-11
     */
    public static String getCurrentDate() {
        CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis());
        return String.valueOf(sysTimeStr);
    }

    /**
     * 返回昨天的日期
     *
     * @return 2017-03-11
     */
    public static String getLastDate() {
        CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd",
                (System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
        return String.valueOf(sysTimeStr);
    }

    /**
     * 返回几天前的日期
     *
     * @param day 天数
     * @return 2017-03-11
     */
    public static String getBeforeDate(int day) {
        CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd",
                (System.currentTimeMillis() - (24 * 60 * 60 * 1000) * day));
        return String.valueOf(sysTimeStr);
    }

    /**
     * 判断当前时间是否大于传入时间2min
     */
    public static boolean isMoreTwoMinutes(String time) {
        long sysTime = System.currentTimeMillis();
        long longTime = 0;
        if (time != null) {
            try {
                Date date = sdf.parse(time);
                longTime = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (sysTime - longTime >= 120000) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")成毫秒数
     */
    public static long parseDate(String time) {
        long longTime = 0;
        if (time != null) {
            try {
                Date date = sdf.parse(time);
                longTime = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return longTime;
    }

    /**
     * 判断钥匙是否在有效期内
     *
     * @param Auth_start_date
     * @param Auth_end_date
     * @return
     */
    public static boolean isInValid(String Auth_start_date, String Auth_end_date) {
        try {
            Date now = new Date();
            Date end_date = sdf.parse(Auth_end_date);
            Date start_date = sdf.parse(Auth_start_date);
            Log.i(TAG, "卡片授权时间:start=" + start_date + ",end=" + end_date);
            return now.before(end_date) && now.after(start_date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i(TAG, "解析授权时间失败");
        }
        return false;
    }

    /**
     * 返回如下格式的时间字符串"yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getUmengFormatTime() {
        return sf.format(new Date());
    }

    /**
     * 判断登录是否过期(默认保持登录为 7 天)
     *
     * @return
     */
    public static boolean isLoginValid() {
        String timestamp = SpUtil.getInstance(CommonApplication.getContext()).getString(
                SpKey.LOGIN_SUCCESS_TIMESTAMP, "");
        if (!TextUtils.isEmpty(timestamp)) {
            long curTime = System.currentTimeMillis();
            long lastLoginTime = Long.parseLong(timestamp);
            return (curTime - lastLoginTime) < (1000 * 60 * 60 * 24 * 7);
        } else {
            return false;
        }
    }
}
