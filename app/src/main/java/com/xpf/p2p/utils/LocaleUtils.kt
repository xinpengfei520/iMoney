package com.xpf.p2p.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import com.google.gson.Gson
import com.xpf.p2p.App
import com.xpf.p2p.constants.SpKey
import java.util.Locale

object LocaleUtils {

    @JvmField
    val LOCALE_CHINESE: Locale = Locale.CHINESE
    @JvmField
    val LOCALE_ENGLISH: Locale = Locale.ENGLISH
    @JvmField
    val LOCALE_RUSSIAN: Locale = Locale("ru")

    private const val LOCALE_FILE = "LOCALE_FILE"
    private const val LOCALE_KEY = "LOCALE_KEY"

    @JvmStatic
    fun getUserLocale(pContext: Context): Locale {
        val currentLanguage = SpUtil.getInstance(App.context)
            .getInt(SpKey.CURRENT_LANGUAGE, 0)
        return when (currentLanguage) {
            0 -> Locale.SIMPLIFIED_CHINESE
            1 -> Locale.ENGLISH
            2 -> Locale.TRADITIONAL_CHINESE
            else -> Locale.SIMPLIFIED_CHINESE
        }
    }

    @JvmStatic
    fun getCurrentLocale(pContext: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pContext.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            pContext.resources.configuration.locale
        }
    }

    @JvmStatic
    fun saveUserLocale(pContext: Context, pUserLocale: Locale) {
        val sharedPreferences = pContext.getSharedPreferences(LOCALE_FILE, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val localeJson = localeToJson(pUserLocale)
        edit.putString(LOCALE_KEY, localeJson)
        edit.apply()
    }

    private fun localeToJson(pUserLocale: Locale): String {
        return Gson().toJson(pUserLocale)
    }

    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun updateLocale(pContext: Context, pNewUserLocale: Locale) {
        if (needUpdateLocale(pContext, pNewUserLocale)) {
            val configuration = pContext.resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(pNewUserLocale)
            } else {
                @Suppress("DEPRECATION")
                configuration.locale = pNewUserLocale
            }
            val displayMetrics = pContext.resources.displayMetrics
            @Suppress("DEPRECATION")
            pContext.resources.updateConfiguration(configuration, displayMetrics)
        }
    }

    @JvmStatic
    fun needUpdateLocale(pContext: Context, pNewUserLocale: Locale?): Boolean {
        return pNewUserLocale != null && getCurrentLocale(pContext) != pNewUserLocale
    }
}
