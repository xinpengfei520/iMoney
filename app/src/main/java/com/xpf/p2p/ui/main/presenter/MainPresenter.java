package com.xpf.p2p.ui.main.presenter;

import com.xpf.common.base.MvpBasePresenter;
import com.xpf.http.logger.XLog;
import com.xpf.p2p.ui.main.contract.MainContract;
import com.xpf.p2p.ui.main.listener.OnCheckUpdateListener;
import com.xpf.p2p.ui.main.model.MainModel;

/**
 * Created by x-sir on 2018/11/1 :)
 * Function:
 */
public class MainPresenter<T extends MainContract.IView>
        extends MvpBasePresenter<T> implements MainContract.IPresenter {

    private static final String TAG = "MainPresenter";
    private MainContract.IModel mModel = new MainModel();


    @Override
    public void checkUpdate() {
        mModel.checkUpdate(new OnCheckUpdateListener() {
            @Override
            public void onSuccess(String url, String description) {
                if (isNonNull()) {
                    mViewRef.get().onUpdateResult(url, description);
                }
            }

            @Override
            public void onFailed() {
                XLog.e("app 检查更新失败！");
            }
        });
    }
}
