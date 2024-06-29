package com.xpf.p2p.adapter;

import android.view.View;


/**
 * Created by xpf on 2016/11/15 :)
 * Function:创建一个ViewHolder的基类,将加载的布局和装配数据全部暴露出去
 */

public abstract class BaseHolder<T> {

    private View rootView;
    private T data;

    public BaseHolder() {
        rootView = initView();
        rootView.setTag(this);
    }

    public View getRootView() {
        return rootView;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        refreshData();
    }

    // 装配数据
    protected abstract void refreshData();

    // 提供item layout
    protected abstract View initView();
}
