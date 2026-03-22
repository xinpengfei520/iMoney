package com.xpf.p2p.utils

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast

/**
 * Created by xpf on 2017/4/15 :)
 * Function:Toast显示工具类
 */
object ToastUtil {

    @JvmStatic
    fun showToastInfo_Fail(context: Context, info: String) {
        val handler = Handler(context.mainLooper)
        handler.post {
            //TastyToast.makeText(context.getApplicationContext(), info, TastyToast.LENGTH_LONG, TastyToast.WARNING);
        }
    }

    @JvmStatic
    fun showToastInfo_Success(context: Context, info: String) {
        val handler = Handler(context.mainLooper)
        handler.post {
            //TastyToast.makeText(context.getApplicationContext(), info, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }
    }

    @JvmStatic
    fun show(context: Context?, text: String?) {
        if (!TextUtils.isEmpty(text) && context != null) {
            Toast.makeText(context.applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    fun showLong(context: Context?, text: String?) {
        if (!TextUtils.isEmpty(text) && context != null) {
            Toast.makeText(context.applicationContext, text, Toast.LENGTH_LONG).show()
        }
    }
}
