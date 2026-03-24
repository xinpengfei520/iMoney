package com.xpf.p2p.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xpf.p2p.R
import com.xpf.p2p.utils.ActivityManager
import com.xpf.p2p.utils.StatusBarUtils

/**
 * 通用 Activity 基类（Kotlin 版本）
 * 非 MVP/MVVM Activity 继承此类
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        supportActionBar?.hide()
        StatusBarUtils.setImmersiveStatusBar(this)
        setupStatusBarSpace()
        ActivityManager.instance.add(this)
        initData()
    }

    protected abstract fun initData()

    protected abstract fun getLayoutId(): Int

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

    fun removeAll() {
        ActivityManager.instance.removeAll()
    }

    private fun setupStatusBarSpace() {
        findViewById<View?>(R.id.status_bar_space)?.let {
            it.layoutParams.height = StatusBarUtils.getStatusBarHeight(this)
        }
    }
}
