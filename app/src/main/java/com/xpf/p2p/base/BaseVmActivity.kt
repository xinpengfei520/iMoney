package com.xpf.p2p.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xpf.p2p.utils.ActivityManager

/**
 * MVVM Activity 基类
 * 使用 ViewModel 替代 Presenter，使用 LiveData 替代回调
 */
abstract class BaseVmActivity<VM : ViewModel> : AppCompatActivity() {

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        supportActionBar?.hide()
        ActivityManager.instance.add(this)
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        initView()
        observeData()
    }

    /** 布局资源 ID */
    protected abstract fun getLayoutId(): Int

    /** ViewModel 类型 */
    protected abstract fun getViewModelClass(): Class<VM>

    /** 初始化视图和事件监听 */
    protected abstract fun initView()

    /** 订阅 LiveData 数据变化 */
    protected open fun observeData() {}

    fun removeCurrentActivity() {
        ActivityManager.instance.removeCurrent()
    }

    fun removeAll() {
        ActivityManager.instance.removeAll()
    }
}
