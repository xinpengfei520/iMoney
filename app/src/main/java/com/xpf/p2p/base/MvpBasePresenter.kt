package com.xpf.p2p.base

import java.lang.ref.WeakReference

/**
 * MVP Presenter 基类（Kotlin 版本）
 * 保留用于尚未迁移到 MVVM 的模块
 */
open class MvpBasePresenter<T> {

    protected var mViewRef: WeakReference<T>? = null

    fun attachView(view: T) {
        mViewRef = WeakReference(view)
    }

    fun detachView() {
        mViewRef?.clear()
    }

    protected fun isNonNull(): Boolean = mViewRef?.get() != null

    protected fun getView(): T? = mViewRef?.get()
}
