package com.xpf.http.core.request;

import com.xpf.http.core.callback.ApiRequestListener;
import com.xpf.http.core.callback.RequestStringCallback;
import com.xpf.http.utils.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;

import okhttp3.MediaType;

/**
 * Created by xpf on 2017/10/24 :)
 * Function:
 */

public class OkhttpFactory {

    /**
     * okhttp get request.
     *
     * @param url
     * @param listener
     */
    public static void get(String url, ApiRequestListener listener) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new RequestStringCallback(listener));
    }

    /**
     * okhttp post request.
     *
     * @param url
     * @param map
     * @param listener
     */
    public static void post(String url, HashMap<String, Object> map, ApiRequestListener listener) {
        OkHttpUtils
                .postString()
                .url(url)
                .content(JsonUtil.toJson(map))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new RequestStringCallback(listener));
    }

	/**
     * okhttp post request (overload method).
     *
     * @param url
     * @param json
     * @param listener
     */
    public static void post(String url, String json, ApiRequestListener listener) {
        OkHttpUtils
                .postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new RequestStringCallback(listener));
    }
}
