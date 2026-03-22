package com.xpf.p2p.jpush

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Looper
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.widget.Toast
import cn.jpush.android.api.JPushInterface
import com.xpf.p2p.utils.LogUtils
import java.util.regex.Pattern

object ExampleUtil {

    private const val TAG = "ExampleUtil"
    private val PATTERN_1 = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#\$&*+=.|]+$")
    private val PATTERN_2 = Pattern.compile("[\\x20-\\x7E]+")

    const val PREFS_NAME = "JPUSH_EXAMPLE"
    const val PREFS_DAYS = "JPUSH_EXAMPLE_DAYS"
    const val PREFS_START_TIME = "PREFS_START_TIME"
    const val PREFS_END_TIME = "PREFS_END_TIME"
    const val KEY_APP_KEY = "JPUSH_APPKEY"

    private const val MOBILE_NUMBER_CHARS = "^[+0-9][-0-9]{1,}$"

    @JvmStatic
    fun isEmpty(s: String?): Boolean {
        if (s == null) return true
        if (s.isEmpty()) return true
        return s.trim().isEmpty()
    }

    @JvmStatic
    fun isValidMobileNumber(s: String?): Boolean {
        if (TextUtils.isEmpty(s)) return true
        val p = Pattern.compile(MOBILE_NUMBER_CHARS)
        val m = p.matcher(s)
        return m.matches()
    }

    @JvmStatic
    fun isValidTagAndAlias(s: String): Boolean {
        val m = PATTERN_1.matcher(s)
        return m.matches()
    }

    @JvmStatic
    fun getAppKey(context: Context): String? {
        var appKey: String? = null
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            val metaData = ai?.metaData
            if (metaData != null) {
                appKey = metaData.getString(KEY_APP_KEY)
                if (appKey == null || appKey.length != 24) {
                    appKey = null
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appKey
    }

    @JvmStatic
    fun GetVersion(context: Context): String {
        return try {
            val manager = context.packageManager.getPackageInfo(context.packageName, 0)
            manager.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    @JvmStatic
    fun showToast(toast: String, context: Context) {
        Thread {
            Looper.prepare()
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
            Looper.loop()
        }.start()
    }

    @JvmStatic
    fun isConnected(context: Context): Boolean {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = conn.activeNetworkInfo
        return info != null && info.isConnected
    }

    @JvmStatic
    fun getImei(context: Context, imei: String): String? {
        var ret: String? = null
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            @Suppress("DEPRECATION")
            ret = telephonyManager.deviceId
        } catch (e: Exception) {
            LogUtils.e(TAG, e.message)
        }
        return if (isReadableASCII(ret)) ret else imei
    }

    private fun isReadableASCII(string: CharSequence?): Boolean {
        if (TextUtils.isEmpty(string)) return false
        return try {
            PATTERN_2.matcher(string).matches()
        } catch (e: Throwable) {
            true
        }
    }

    @JvmStatic
    fun getDeviceId(context: Context): String {
        return JPushInterface.getUdid(context)
    }
}
