package com.xpf.p2p.activity

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.databinding.ActivityAccountSafeBinding

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:账户安全页面
 */
class AccountSafeActivity : BaseActivity<ActivityAccountSafeBinding>() {

    override fun createViewBinding(inflater: LayoutInflater) = ActivityAccountSafeBinding.inflate(inflater)

    override fun initData() {
        binding.titleBar.ivBack.visibility = View.VISIBLE
        binding.titleBar.ivSetting.visibility = View.GONE
        binding.titleBar.tvTitle.text = "账户安全设置"

        binding.titleBar.ivBack.setOnClickListener { removeCurrentActivity() }
        binding.btnResetGesture.setOnClickListener {
            goToActivity(GestureEditActivity::class.java, null)
        }

        val sp = getSharedPreferences("secret_protect", Context.MODE_PRIVATE)
        val isOpen = sp.getBoolean("isOpen", false)
        binding.togglebutton.isChecked = isOpen
        binding.btnResetGesture.isEnabled = isOpen

        binding.togglebutton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val inputCode = sp.getString("inputCode", "")
                if (TextUtils.isEmpty(inputCode)) {
                    Toast.makeText(this, "开启手势解锁", Toast.LENGTH_SHORT).show()
                    AlertDialog.Builder(this)
                        .setTitle("是否现在设置手势密码")
                        .setPositiveButton("确定") { _, _ ->
                            val inputCode1 = sp.getString("inputCode", "")
                            if (TextUtils.isEmpty(inputCode1)) {
                                Toast.makeText(this, "现在设置手势密码", Toast.LENGTH_SHORT).show()
                                binding.btnResetGesture.isEnabled = true
                                sp.edit().putBoolean("isOpen", true).apply()
                                goToActivity(GestureEditActivity::class.java, null)
                            }
                        }
                        .setNegativeButton("取消") { _, _ ->
                            Toast.makeText(this, "取消设置手势密码", Toast.LENGTH_SHORT).show()
                            binding.togglebutton.isChecked = false
                            binding.btnResetGesture.isEnabled = false
                            sp.edit().putBoolean("isOpen", false).apply()
                        }.show()
                } else {
                    Toast.makeText(this, "开启了手势密码", Toast.LENGTH_SHORT).show()
                    binding.btnResetGesture.isEnabled = true
                    sp.edit().putBoolean("isOpen", true).apply()
                }
            } else {
                Toast.makeText(this, "关闭手势解锁", Toast.LENGTH_SHORT).show()
                sp.edit().putBoolean("isOpen", false).apply()
                binding.btnResetGesture.isEnabled = false
            }
        }
    }
}
