package com.xpf.p2p.ui.login.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import com.xpf.p2p.utils.LogUtils
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseVmActivity
import com.xpf.p2p.databinding.ActivityLoginBinding
import com.xpf.p2p.ui.login.LoginViewModel
import com.xpf.p2p.ui.main.view.MainActivity
import com.xpf.p2p.utils.ToastUtil

/**
 * 登录页面 — MVVM 版本
 */
class LoginActivity : BaseVmActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun createViewBinding(inflater: LayoutInflater) = ActivityLoginBinding.inflate(inflater)

    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initView() {
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
                binding.tvSendSmsCode.visibility = View.GONE
                binding.countDownView.visibility = View.VISIBLE
                binding.countDownView.start(60000)
            }
        }
    }

    private fun initListener() {
        binding.logLogBtn.setOnClickListener {
            viewModel.verifySmsCode(binding.logEdMob.text.toString(), binding.logEdPad.text.toString())
        }
        binding.tvTestUse.setOnClickListener { loginSuccess() }
        binding.tvSendSmsCode.setOnClickListener {
            viewModel.sendSmsCode(binding.logEdMob.text.toString())
        }
        binding.countDownView.setOnCountdownEndListener {
            binding.countDownView.visibility = View.GONE
            binding.tvSendSmsCode.visibility = View.VISIBLE
            binding.tvSendSmsCode.setText(R.string.resend)
        }
        binding.logEdMob.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvSendSmsCode.visibility = if (s?.length != 11) View.GONE else View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.logEdPad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.logLogBtn.isEnabled = !s.isNullOrEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        removeCurrentActivity()
    }

    companion object {
        @JvmStatic
        fun actionStart(context: Context?) {
            if (context != null) {
                context.startActivity(Intent(context, LoginActivity::class.java))
                (context as? Activity)?.finish()
            } else {
                LogUtils.e("LoginActivity", "context is null!")
            }
        }
    }
}
