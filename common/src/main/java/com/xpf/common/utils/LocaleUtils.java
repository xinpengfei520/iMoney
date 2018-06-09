package com.xpf.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.xpf.common.CommonApplication;
import com.xpf.common.cons.SpKey;

import java.util.Locale;

public class LocaleUtils {
    /**
     * 中文
     */
    public static final Locale LOCALE_CHINESE = Locale.CHINESE;
    /**
     * 英文
     */
    public static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
    /**
     * 俄文
     */
    public static final Locale LOCALE_RUSSIAN = new Locale("ru");
    /**
     * 保存SharedPreferences的文件名
     */
    private static final String LOCALE_FILE = "LOCALE_FILE";
    /**
     * 保存Locale的key
     */
    private static final String LOCALE_KEY = "LOCALE_KEY";

    /**
     * 获取用户设置的Locale
     *
     * @param pContext Context
     * @return Locale
     */
    public static Locale getUserLocale(Context pContext) {
        int currentLanguage = SpUtil.getInstance(CommonApplication.getContext())
                .getInt(SpKey.CURRENT_LANGUAGE, 0);
        Locale myLocale = Locale.SIMPLIFIED_CHINESE;
        switch (currentLanguage) {
            case 0:
                myLocale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 1:
                myLocale = Locale.ENGLISH;
                break;
            case 2:
                myLocale = Locale.TRADITIONAL_CHINESE;
                break;
        }
        return myLocale;
    }

    /**
     * 获取当前的Locale
     *
     * @param pContext Context
     * @return Locale
     */
    public static Locale getCurrentLocale(Context pContext) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
            locale = pContext.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = pContext.getResources().getConfiguration().locale;
        }
        return locale;
    }

    /**
     * 保存用户设置的Locale
     *
     * @param pContext    Context
     * @param pUserLocale Locale
     */
    public static void saveUserLocale(Context pContext, Locale pUserLocale) {
        SharedPreferences sharedPreferences = pContext.getSharedPreferences(LOCALE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String localeJson = localeToJson(pUserLocale);
        edit.putString(LOCALE_KEY, localeJson);
        edit.apply();
    }

    /**
     * Locale转成json
     *
     * @param pUserLocale UserLocale
     * @return json String
     */
    private static String localeToJson(Locale pUserLocale) {
        return new Gson().toJson(pUserLocale);
    }

    /**
     * 更新Locale
     *
     * @param pContext       Context
     * @param pNewUserLocale New User Locale
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void updateLocale(Context pContext, Locale pNewUserLocale) {
        if (needUpdateLocale(pContext, pNewUserLocale)) {
            Configuration configuration = pContext.getResources().getConfiguration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(pNewUserLocale);
            } else {
                configuration.locale = pNewUserLocale;
            }
            DisplayMetrics displayMetrics = pContext.getResources().getDisplayMetrics();
            pContext.getResources().updateConfiguration(configuration, displayMetrics);
            //saveUserLocale(pContext, pNewUserLocale);
        }
    }

    /**
     * 判断需不需要更新
     *
     * @param pContext       Context
     * @param pNewUserLocale New User Locale
     * @return true / false
     */
    public static boolean needUpdateLocale(Context pContext, Locale pNewUserLocale) {
        return pNewUserLocale != null && !getCurrentLocale(pContext).equals(pNewUserLocale);
    }
}
