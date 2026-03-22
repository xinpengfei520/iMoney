package com.xpf.p2p.adapter

import android.view.View

/**
 * Created by xpf on 2016/11/15 :)
 * Function:创建一个ViewHolder的基类,将加载的布局和装配数据全部暴露出去
 */
abstract class BaseHolder<T> {

    val rootView: View = initView().also { it.tag = this }
    var data: T? = null
        set(value) {
            field = value
            refreshData()
        }

    protected abstract fun refreshData()

    protected abstract fun initView(): View
}
