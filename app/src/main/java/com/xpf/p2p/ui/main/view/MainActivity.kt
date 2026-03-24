package com.xpf.p2p.ui.main.view

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import com.xpf.p2p.R
import com.xpf.p2p.activity.UserInfoActivity
import com.xpf.p2p.base.BaseVmActivity
import com.xpf.p2p.databinding.ActivityMainBinding
import com.xpf.p2p.fragment.HomeFragment2
import com.xpf.p2p.fragment.InvestFragment
import com.xpf.p2p.fragment.MeFragment
import com.xpf.p2p.fragment.MoreFragment
import com.xpf.p2p.ui.main.MainViewModel
import com.xpf.p2p.utils.ToastUtil
import com.xsir.pgyerappupdate.library.PgyerApi
import me.ele.uetool.UETool

/**
 * 主页面 — MVVM 版本
 */
class MainActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {

    private var homeFragment: HomeFragment2? = null
    private var investFragment: InvestFragment? = null
    private var meFragment: MeFragment? = null
    private var moreFragment: MoreFragment? = null

    private var isFlag = true
    private val handler = Handler(Looper.getMainLooper())

    override fun createViewBinding(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun initView() {
        binding.titleBar.ivSetting.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }
        binding.bottomBar.rbHome.setOnClickListener { setSelect(0) }
        binding.bottomBar.rbInvest.setOnClickListener { setSelect(1) }
        binding.bottomBar.rbAssets.setOnClickListener { setSelect(2) }
        binding.bottomBar.rbMore.setOnClickListener { setSelect(3) }

        UETool.showUETMenu()
        binding.bottomBar.rgMain.check(R.id.rb_home)
        setSelect(0)
        PgyerApi.checkUpdate(this)
    }

    private fun setSelect(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        // 隐藏所有 Fragment
        homeFragment?.let { transaction.hide(it) }
        investFragment?.let { transaction.hide(it) }
        meFragment?.let { transaction.hide(it) }
        moreFragment?.let { transaction.hide(it) }

        when (index) {
            0 -> {
                binding.titleBar.tvTitle.text = getString(R.string.rb_home)
                binding.titleBar.ivBack.visibility = View.INVISIBLE
                binding.titleBar.ivSetting.visibility = View.INVISIBLE
                if (homeFragment == null) {
                    homeFragment = HomeFragment2()
                    transaction.add(R.id.fl_main, homeFragment!!)
                }
                transaction.show(homeFragment!!)
            }
            1 -> {
                binding.titleBar.tvTitle.text = getString(R.string.rb_invest)
                binding.titleBar.ivSetting.visibility = View.INVISIBLE
                binding.titleBar.ivBack.visibility = View.INVISIBLE
                if (investFragment == null) {
                    investFragment = InvestFragment()
                    transaction.add(R.id.fl_main, investFragment!!)
                }
                transaction.show(investFragment!!)
            }
            2 -> {
                binding.titleBar.tvTitle.text = getString(R.string.rb_my_assets)
                binding.titleBar.ivSetting.visibility = View.VISIBLE
                binding.titleBar.ivBack.visibility = View.INVISIBLE
                if (meFragment == null) {
                    meFragment = MeFragment()
                    transaction.add(R.id.fl_main, meFragment!!)
                }
                transaction.show(meFragment!!)
            }
            3 -> {
                binding.titleBar.tvTitle.text = getString(R.string.rb_more)
                binding.titleBar.ivSetting.visibility = View.INVISIBLE
                binding.titleBar.ivBack.visibility = View.INVISIBLE
                if (moreFragment == null) {
                    moreFragment = MoreFragment()
                    transaction.add(R.id.fl_main, moreFragment!!)
                }
                transaction.show(moreFragment!!)
            }
        }
        transaction.commit()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && isFlag) {
            isFlag = false
            ToastUtil.show(this, getString(R.string.toast_exit_click_again))
            handler.postDelayed({ isFlag = true }, 2000)
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
