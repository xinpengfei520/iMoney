package com.xpf.p2p.utils

import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import com.xpf.p2p.App
import com.xpf.p2p.constants.SpKey
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by xpf on 2017/4/15 :)
 * Function:获取时间的工具类
 */
object TimeUtil {

    private val TAG = TimeUtil::class.java.simpleName
    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @JvmStatic
    fun getCurrentTime(): String {
        val sysTimeStr = DateFormat.format("yyyyMMdd-HH:mm:ss", System.currentTimeMillis())
        return sysTimeStr.toString()
    }

    @JvmStatic
    fun getCurrentDay(): String {
        val sysTimeStr = DateFormat.format("yyyyMMdd", System.currentTimeMillis())
        return sysTimeStr.toString()
    }

    @JvmStatic
    fun getCurrentDate(): String {
        val sysTimeStr = DateFormat.format("yyyy-MM-dd", System.currentTimeMillis())
        return sysTimeStr.toString()
    }

    @JvmStatic
    fun getLastDate(): String {
        val sysTimeStr = DateFormat.format("yyyy-MM-dd",
            System.currentTimeMillis() - (24 * 60 * 60 * 1000))
        return sysTimeStr.toString()
    }

    @JvmStatic
    fun getBeforeDate(day: Int): String {
        val sysTimeStr = DateFormat.format("yyyy-MM-dd",
            System.currentTimeMillis() - (24L * 60 * 60 * 1000) * day)
        return sysTimeStr.toString()
    }

    @JvmStatic
    fun isMoreTwoMinutes(time: String?): Boolean {
        val sysTime = System.currentTimeMillis()
        if (time != null) {
            try {
                val date = sdf.parse(time)
                val longTime = date?.time ?: 0
                if (sysTime - longTime >= 120000) {
                    return true
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return false
    }

    @JvmStatic
    fun parseDate(time: String?): Long {
        var longTime: Long = 0
        if (time != null) {
            try {
                val date = sdf.parse(time)
                longTime = date?.time ?: 0
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return longTime
    }

    @JvmStatic
    fun isInValid(Auth_start_date: String, Auth_end_date: String): Boolean {
        try {
            val now = Date()
            val end_date = sdf.parse(Auth_end_date)
            val start_date = sdf.parse(Auth_start_date)
            Log.i(TAG, "卡片授权时间:start=$start_date,end=$end_date")
            return now.before(end_date) && now.after(start_date)
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.i(TAG, "解析授权时间失败")
        }
        return false
    }

    @JvmStatic
    fun getUmengFormatTime(): String {
        return sf.format(Date())
    }

    @JvmStatic
    fun isLoginValid(): Boolean {
        val timestamp = SpUtil.getInstance(App.context).getString(
            SpKey.LOGIN_SUCCESS_TIMESTAMP, "")
        if (!TextUtils.isEmpty(timestamp)) {
            val curTime = System.currentTimeMillis()
            val lastLoginTime = timestamp!!.toLong()
            return (curTime - lastLoginTime) < (1000L * 60 * 60 * 24 * 7)
        } else {
            return false
        }
    }
}
