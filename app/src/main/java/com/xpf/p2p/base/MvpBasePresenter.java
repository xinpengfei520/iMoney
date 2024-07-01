package com.xpf.p2p.base;

import java.lang.ref.WeakReference;

/**
 * Created by xpf on 2018/5/4 :)
 * Function:
 */
public class MvpBasePresenter<T> {

    protected WeakReference<T> mViewRef;

    /**
     * attach view reference
     *
     * @param view
     */
    public void attachView(T view) {
        mViewRef = new WeakReference<>(view);
    }

    /**
     * detach view reference
     */
    public void detachView() {
        mViewRef.clear();
    }

    /**
     * is or not nullable
     */
    protected boolean isNonNull() {
        return mViewRef.get() != null;
    }

    /**
     * get view
     */
    protected T getView() {
        return mViewRef.get();
    }
}
