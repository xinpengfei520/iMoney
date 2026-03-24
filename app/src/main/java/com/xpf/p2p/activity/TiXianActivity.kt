package com.xpf.p2p.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.databinding.ActivityTiXianBinding
import com.xpf.p2p.utils.UIUtils

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:提现页面
 */
class TiXianActivity : BaseActivity<ActivityTiXianBinding>() {

    override fun createViewBinding(inflater: LayoutInflater) = ActivityTiXianBinding.inflate(inflater)

    override fun initData() {
        binding.titleBar.ivBack.visibility = View.VISIBLE
        binding.titleBar.tvTitle.text = "提现"
        binding.titleBar.ivSetting.visibility = View.GONE

        binding.titleBar.ivBack.setOnClickListener { removeCurrentActivity() }
        binding.btnTixian.setOnClickListener { withdraw() }

        binding.btnTixian.isClickable = false
        binding.inputMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val moneyNum = binding.inputMoney.text.toString().trim()
                if (TextUtils.isEmpty(moneyNum)) {
                    binding.btnTixian.setBackgroundResource(R.drawable.btn_023)
                    binding.btnTixian.isClickable = false
                } else {
                    binding.btnTixian.setBackgroundResource(R.drawable.btn_01)
                    binding.btnTixian.isClickable = true
                }
            }
        })
    }

    private fun withdraw() {
        Toast.makeText(this, "您的提现请求发送成功,请您稍后查询到账信息", Toast.LENGTH_SHORT).show()
        UIUtils.getHandler().postDelayed({ removeCurrentActivity() }, 2000)
    }
}
