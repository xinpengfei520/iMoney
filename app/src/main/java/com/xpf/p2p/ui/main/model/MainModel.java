package com.xpf.p2p.ui.main.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xpf.common.bean.UpdateAppInfo;
import com.xpf.common.cons.ApiRequestUrl;
import com.xpf.http.core.Okla;
import com.xpf.http.core.callback.ApiRequestListener;
import com.xpf.http.logger.XLog;
import com.xpf.p2p.P2PApplication;
import com.xpf.p2p.ui.main.contract.MainContract;
import com.xpf.p2p.ui.main.listener.OnCheckUpdateListener;
import com.xpf.p2p.utils.AppUtil;

import java.util.HashMap;

/**
 * Created by x-sir on 2018/11/1 :)
 * Function:
 */
public class MainModel implements MainContract.IModel {

    private static final String TAG = "MainModel";

    public MainModel() {
    }

    @Override
    public void checkUpdate(OnCheckUpdateListener listener) {
        String apiKey = "534a49154990d8e9126918fbdbee611a";
        String appKey = "83690c2bcf44f58758791a3023f91c91";

        // {"code":1001,"message":"_api_key could not be empty"}
        HashMap<String, Object> map = new HashMap<>();
        map.put("_api_key", apiKey);
        map.put("appKey", appKey);
        map.put("buildVersion", "");
        map.put("buildBuildVersion", "");
        String json = new Gson().toJson(map);
        XLog.i("checkUpdate() -> post json===" + json);

        // post form 表单就相当于是 get 拼接参数的形式
        String url = ApiRequestUrl.PGYER_CHECK_UPDATE + "?_api_key=" + apiKey + "&appKey=" + appKey;

        Okla.request()
                .get(url, new ApiRequestListener() {
                    @Override
                    public void onSuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            UpdateAppInfo updateAppInfo = new Gson().fromJson(result, UpdateAppInfo.class);
                            if (updateAppInfo != null) {
                                int code = updateAppInfo.getCode();
                                if (code == 0) {
                                    UpdateAppInfo.DataBean dataBean = updateAppInfo.getData();
                                    if (dataBean != null) {
                                        String url = dataBean.getDownloadURL();
                                        int versionNo = dataBean.getBuildVersionNo();
                                        String buildUpdateDescription = dataBean.getBuildUpdateDescription();
                                        XLog.i("updateApp===" + versionNo + "," + buildUpdateDescription + "," + url);
                                        if (versionNo > AppUtil.getAppVersionCode(P2PApplication.getContext())) {
                                            if (listener != null) {
                                                listener.onSuccess(url, buildUpdateDescription);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String ex) {
                        if (listener != null) {
                            listener.onFailed();
                        }
                    }
                });
    }
}
