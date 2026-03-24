package com.xpf.p2p.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.xpf.p2p.R
import com.xpf.p2p.utils.ActivityManager
import com.xpf.p2p.utils.StatusBarUtils

/**
 * MVP Activity 基类（Kotlin 版本）
 * 保留用于尚未迁移到 MVVM 的模块
 */
abstract class MvpBaseActivity<VB : ViewBinding, V, T : MvpBasePresenter<V>> : AppCompatActivity() {

    protected lateinit var binding: VB
    lateinit var mPresenter: T

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = createPresenter()
        mPresenter.attachView(this as V)
        binding = createViewBinding(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        StatusBarUtils.setImmersiveStatusBar(this)
        setupStatusBarSpace()
        ActivityManager.instance.add(this)
        initData()
    }

    protected abstract fun createPresenter(): T

    protected abstract fun createViewBinding(inflater: LayoutInflater): VB

    protected abstract fun initData()

    fun removeCurrentActivity() {
        ActivityManager.instance.removeCurrent()
    }

    fun goToActivity(activity: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, activity)
        if (bundle != null && !bundle.isEmpty) {
            intent.putExtra("data", bundle)
        }
        startActivity(intent)
    }

    protected fun removeAll() {
        ActivityManager.instance.removeAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private fun setupStatusBarSpace() {
        findViewById<View?>(R.id.status_bar_space)?.let {
            it.layoutParams.height = StatusBarUtils.getStatusBarHeight(this)
        }
    }
}
