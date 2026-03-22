package com.xpf.p2p.utils

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by xpf on 2017/5/11 :)
 * Function:网络请求的工具类
 */
object RequestUtil {

    private val TAG = RequestUtil::class.java.simpleName

    @JvmStatic
    fun getName(json: String): String {
        var name = ""
        try {
            val jsonObject = JSONObject(json)
            name = jsonObject.getString("name")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return name
    }

    @JvmStatic
    fun getCode(json: String): String {
        var code = ""
        try {
            val jsonObject = JSONObject(json)
            code = jsonObject.getString("code")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return code
    }
}
