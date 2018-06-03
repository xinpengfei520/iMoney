package com.xpf.http;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by xpf on 2017/10/23 :)
 * Function:okhttp request string callback.
 */

public class RequestStringCallback extends StringCallback {

    private ApiRequestListener listener;

    public RequestStringCallback(ApiRequestListener listener) {
        this.listener = listener;
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        XLog.e("onError===" + e.toString());
        if (listener != null) {
            listener.onError(e.toString());
        }
    }

    @Override
    public void onResponse(String s, int id) {
        XLog.i("onResponse===" + s);
        if (listener != null) {
            listener.onSuccess(s);
        }
    }
}
