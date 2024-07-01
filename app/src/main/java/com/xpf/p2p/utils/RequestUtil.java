package com.xpf.p2p.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xpf on 2017/5/11 :)
 * Function:网络请求的工具类
 */

public class RequestUtil {

    private static final String TAG = RequestUtil.class.getSimpleName();

    /**
     * 获取Json的name
     *
     * @param json
     * @return
     */
    public static String getName(String json) {
        JSONObject jsonObject = null;
        String name = "";
        try {
            jsonObject = new JSONObject(json);
            name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取网络请求的code码
     */
    public static String getCode(String json) {
        JSONObject jsonObject = null;
        String code = "";
        try {
            jsonObject = new JSONObject(json);
            code = jsonObject.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return code;
    }

}
