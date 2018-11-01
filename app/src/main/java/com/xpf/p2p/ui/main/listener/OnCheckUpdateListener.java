package com.xpf.p2p.ui.main.listener;

/**
 * Created by x-sir on 2018/11/1 :)
 * Function:
 */
public interface OnCheckUpdateListener {

    void onSuccess(String url, String description);

    void onFailed();
}
