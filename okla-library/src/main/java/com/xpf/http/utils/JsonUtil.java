package com.xpf.http.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xpf on 2017/11/29 :)
 * Function:HashMap convert to json.
 */

public class JsonUtil {

    /**
     * map convert to json string.
     *
     * @param map
     * @return
     */
    public static String toJson(HashMap<String, Object> map) {
        if (map != null && map.size() > 0) {
            JSONObject loginObject = new JSONObject();
            // 通过Map.entrySet遍历key和value"
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Log.e("TAG", "key= " + entry.getKey() + " and value= " + entry.getValue());
                try {
                    loginObject.put(entry.getKey(), entry.getValue());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            return loginObject.toString();
        } else {
            return "";
        }
    }
}
