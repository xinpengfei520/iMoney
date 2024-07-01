package com.xpf.p2p.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by xpf on 2017/4/15 :)
 * Function:Toast显示工具类
 */
public class ToastUtil {

    public static void showToastInfo_Fail(final Context context, final String info) {
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                //TastyToast.makeText(context.getApplicationContext(), info, TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }
        });
    }

    public static void showToastInfo_Success(final Context context, final String info) {
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                //TastyToast.makeText(context.getApplicationContext(), info, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
            }
        });
    }

    /**
     * 显示short Toast
     *
     * @param text
     */
    public static void show(Context context, String text) {
        if (!TextUtils.isEmpty(text) && context != null) {
            Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示long Toast
     *
     * @param text
     */
    public static void showLong(Context context, String text) {
        if (!TextUtils.isEmpty(text) && context != null) {
            Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
    }
}
