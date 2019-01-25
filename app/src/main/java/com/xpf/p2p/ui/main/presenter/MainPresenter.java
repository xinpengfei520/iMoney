package com.xpf.p2p.ui.main.presenter;

import com.xpf.common.base.MvpBasePresenter;
import com.xpf.p2p.ui.main.contract.MainContract;
import com.xpf.p2p.ui.main.model.MainModel;

/**
 * Created by x-sir on 2018/11/1 :)
 * Function:
 */
public class MainPresenter<T extends MainContract.IView>
        extends MvpBasePresenter<T> implements MainContract.IPresenter {

    private static final String TAG = "MainPresenter";
    private MainContract.IModel mModel = new MainModel();
}
