package com.xpf.p2p.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by xpf on 2017/03/25 :)
 * Function: sp存储的工具类
 */
class SpUtil private constructor() {

    fun save(key: String, value: Any) {
        when (value) {
            is String -> mSp!!.edit().putString(key, value).apply()
            is Boolean -> mSp!!.edit().putBoolean(key, value).apply()
            is Int -> mSp!!.edit().putInt(key, value).apply()
        }
    }

    fun getString(key: String, defValue: String): String? {
        return mSp!!.getString(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mSp!!.getBoolean(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return mSp!!.getInt(key, defValue)
    }

    fun clearAll() {
        mSp!!.edit().clear().apply()
    }

    companion object {
        private const val SP_DATA = "sp_data"
        private val instance = SpUtil()
        private var mSp: SharedPreferences? = null

        @JvmStatic
        fun getInstance(context: Context?): SpUtil {
            if (mSp == null) {
                if (context != null) {
                    mSp = context.applicationContext.getSharedPreferences(SP_DATA, Context.MODE_PRIVATE)
                }
            }
            return instance
        }
    }
}
