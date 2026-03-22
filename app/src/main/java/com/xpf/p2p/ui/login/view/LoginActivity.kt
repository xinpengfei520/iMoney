package com.xpf.p2p.ui.login.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import cn.iwgang.countdownview.CountdownView
import com.xpf.http.logger.XLog
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseVmActivity
import com.xpf.p2p.ui.login.LoginViewModel
import com.xpf.p2p.ui.main.view.MainActivity
import com.xpf.p2p.utils.ToastUtil

/**
 * 登录页面 — MVVM 版本
 */
class LoginActivity : BaseVmActivity<LoginViewModel>() {

    private lateinit var logEdMob: EditText
    private lateinit var tvTestUse: TextView
    private lateinit var logEdPad: EditText
    private lateinit var logLogBtn: Button
    private lateinit var tvSendSmsCode: TextView
    private lateinit var countDownView: CountdownView

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initView() {
        logEdMob = findViewById(R.id.log_ed_mob)
        tvTestUse = findViewById(R.id.tvTestUse)
        logEdPad = findViewById(R.id.log_ed_pad)
        logLogBtn = findViewById(R.id.log_log_btn)
        tvSendSmsCode = findViewById(R.id.tvSendSmsCode)
        countDownView = findViewById(R.id.countDownView)
        initListener()
    }

    override fun observeData() {
        viewModel.smsSendResult.observe(this) { success ->
            if (success) {
                ToastUtil.show(this, "发送成功~")
            } else {
                ToastUtil.show(this, "发送失败!")
            }
        }

        viewModel.loginResult.observe(this) { success ->
            if (success) {
                ToastUtil.show(this, "登录成功~")
                loginSuccess()
            } else {
                ToastUtil.show(this, "验证码不正确！")
            }
        }

        viewModel.errorMessage.observe(this) { msg ->
            ToastUtil.show(this, msg)
        }

        viewModel.startCountDown.observe(this) { start ->
            if (start) {
                tvSendSmsCode.visibility = View.GONE
                countDownView.visibility = View.VISIBLE
                countDownView.start(60000)
            }
        }
    }

    private fun initListener() {
        logLogBtn.setOnClickListener {
            viewModel.verifySmsCode(logEdMob.text.toString(), logEdPad.text.toString())
        }
        tvTestUse.setOnClickListener { loginSuccess() }
        tvSendSmsCode.setOnClickListener {
            viewModel.sendSmsCode(logEdMob.text.toString())
        }
        countDownView.setOnCountdownEndListener {
            countDownView.visibility = View.GONE
            tvSendSmsCode.visibility = View.VISIBLE
            tvSendSmsCode.setText(R.string.resend)
        }
        logEdMob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvSendSmsCode.visibility = if (s?.length != 11) View.GONE else View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        logEdPad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                logLogBtn.isEnabled = !s.isNullOrEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        removeCurrentActivity()
    }

    companion object {
        fun actionStart(context: Context?) {
            if (context != null) {
                context.startActivity(Intent(context, LoginActivity::class.java))
                (context as? Activity)?.finish()
            } else {
                XLog.e("context is null!")
            }
        }
    }
}
