package com.xpf.p2p.activity

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseActivity

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:账户安全页面
 */
class AccountSafeActivity : BaseActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivSetting: ImageView
    private lateinit var togglebutton: ToggleButton
    private lateinit var btnResetGesture: Button
    private lateinit var activityAccountSafe: LinearLayout

    override fun getLayoutId(): Int = R.layout.activity_account_safe

    override fun initData() {
        ivBack = findViewById(R.id.iv_back)
        tvTitle = findViewById(R.id.tv_title)
        ivSetting = findViewById(R.id.iv_setting)
        togglebutton = findViewById(R.id.togglebutton)
        btnResetGesture = findViewById(R.id.btn_resetGesture)
        activityAccountSafe = findViewById(R.id.activity_account_safe)
        ivBack.visibility = View.VISIBLE
        ivSetting.visibility = View.GONE
        tvTitle.text = "账户安全设置"

        ivBack.setOnClickListener { removeCurrentActivity() }
        btnResetGesture.setOnClickListener {
            goToActivity(GestureEditActivity::class.java, null)
        }

        val sp = getSharedPreferences("secret_protect", Context.MODE_PRIVATE)
        val isOpen = sp.getBoolean("isOpen", false)
        togglebutton.isChecked = isOpen
        btnResetGesture.isEnabled = isOpen

        togglebutton.setOnCheckedChangeListener { _, isChecked ->
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
                                btnResetGesture.isEnabled = true
                                sp.edit().putBoolean("isOpen", true).apply()
                                goToActivity(GestureEditActivity::class.java, null)
                            }
                        }
                        .setNegativeButton("取消") { _, _ ->
                            Toast.makeText(this, "取消设置手势密码", Toast.LENGTH_SHORT).show()
                            togglebutton.isChecked = false
                            btnResetGesture.isEnabled = false
                            sp.edit().putBoolean("isOpen", false).apply()
                        }.show()
                } else {
                    Toast.makeText(this, "开启了手势密码", Toast.LENGTH_SHORT).show()
                    btnResetGesture.isEnabled = true
                    sp.edit().putBoolean("isOpen", true).apply()
                }
            } else {
                Toast.makeText(this, "关闭手势解锁", Toast.LENGTH_SHORT).show()
                sp.edit().putBoolean("isOpen", false).apply()
                btnResetGesture.isEnabled = false
            }
        }
    }
}
