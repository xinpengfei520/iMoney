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
 * 通用 Activity 基类（Kotlin 版本）
 * 非 MVP/MVVM Activity 继承此类
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createViewBinding(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        StatusBarUtils.setImmersiveStatusBar(this)
        setupStatusBarSpace()
        ActivityManager.instance.add(this)
        initData()
    }

    protected abstract fun initData()

    protected abstract fun createViewBinding(inflater: LayoutInflater): VB

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
