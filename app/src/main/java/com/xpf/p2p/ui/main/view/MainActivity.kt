package com.xpf.p2p.ui.main.view

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.xpf.p2p.R
import com.xpf.p2p.activity.UserInfoActivity
import com.xpf.p2p.base.BaseVmActivity
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
class MainActivity : BaseVmActivity<MainViewModel>() {

    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivSetting: ImageView
    private lateinit var flMain: FrameLayout
    private lateinit var rbHome: RadioButton
    private lateinit var rbInvest: RadioButton
    private lateinit var rbAssets: RadioButton
    private lateinit var rbMore: RadioButton
    private lateinit var rgMain: RadioGroup

    private var homeFragment: HomeFragment2? = null
    private var investFragment: InvestFragment? = null
    private var meFragment: MeFragment? = null
    private var moreFragment: MoreFragment? = null

    private var isFlag = true
    private val handler = Handler(Looper.getMainLooper())

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun initView() {
        ivBack = findViewById(R.id.iv_back)
        tvTitle = findViewById(R.id.tv_title)
        ivSetting = findViewById(R.id.iv_setting)
        flMain = findViewById(R.id.fl_main)
        rbHome = findViewById(R.id.rb_home)
        rbInvest = findViewById(R.id.rb_invest)
        rbAssets = findViewById(R.id.rb_assets)
        rbMore = findViewById(R.id.rb_more)
        rgMain = findViewById(R.id.rg_main)

        ivSetting.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }
        rbHome.setOnClickListener { setSelect(0) }
        rbInvest.setOnClickListener { setSelect(1) }
        rbAssets.setOnClickListener { setSelect(2) }
        rbMore.setOnClickListener { setSelect(3) }

        UETool.showUETMenu()
        rgMain.check(R.id.rb_home)
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
                tvTitle.text = getString(R.string.rb_home)
                ivBack.visibility = View.INVISIBLE
                ivSetting.visibility = View.INVISIBLE
                if (homeFragment == null) {
                    homeFragment = HomeFragment2()
                    transaction.add(R.id.fl_main, homeFragment!!)
                }
                transaction.show(homeFragment!!)
            }
            1 -> {
                tvTitle.text = getString(R.string.rb_invest)
                ivSetting.visibility = View.INVISIBLE
                ivBack.visibility = View.INVISIBLE
                if (investFragment == null) {
                    investFragment = InvestFragment()
                    transaction.add(R.id.fl_main, investFragment!!)
                }
                transaction.show(investFragment!!)
            }
            2 -> {
                tvTitle.text = getString(R.string.rb_my_assets)
                ivSetting.visibility = View.VISIBLE
                ivBack.visibility = View.INVISIBLE
                if (meFragment == null) {
                    meFragment = MeFragment()
                    transaction.add(R.id.fl_main, meFragment!!)
                }
                transaction.show(meFragment!!)
            }
            3 -> {
                tvTitle.text = getString(R.string.rb_more)
                ivSetting.visibility = View.INVISIBLE
                ivBack.visibility = View.INVISIBLE
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
