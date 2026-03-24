package com.xpf.p2p.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xpf.p2p.R
import com.xpf.p2p.utils.ActivityManager
import com.xpf.p2p.utils.StatusBarUtils

/**
 * MVP Activity 基类（Kotlin 版本）
 * 保留用于尚未迁移到 MVVM 的模块
 */
abstract class MvpBaseActivity<V, T : MvpBasePresenter<V>> : AppCompatActivity() {

    lateinit var mPresenter: T

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = createPresenter()
        mPresenter.attachView(this as V)
        setContentView(getLayoutId())
        supportActionBar?.hide()
        StatusBarUtils.setImmersiveStatusBar(this)
        setupStatusBarSpace()
        ActivityManager.instance.add(this)
        initData()
    }

    protected abstract fun createPresenter(): T

    protected abstract fun getLayoutId(): Int

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
