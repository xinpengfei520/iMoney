package com.xpf.p2p.ui.main.contract;

import com.xpf.p2p.ui.main.listener.OnCheckUpdateListener;

/**
 * Created by x-sir on 2018/11/1 :)
 * Function:
 */
public interface MainContract {
    interface IModel {
        void checkUpdate(OnCheckUpdateListener listener);
    }

    interface IView {
        void onUpdateResult(String url, String description);
    }

    interface IPresenter {
        void checkUpdate();
    }
}
