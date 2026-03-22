package com.xpf.p2p.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.utils.UIUtils

/**
 * Created by x-sir on 2016/8/3 :)
 * Function:提现页面
 */
class TiXianActivity : BaseActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivSetting: ImageView
    private lateinit var accountZhifubao: TextView
    private lateinit var selectBank: RelativeLayout
    private lateinit var chongzhiText: TextView
    private lateinit var dividerView: View
    private lateinit var inputMoney: EditText
    private lateinit var chongzhiText2: TextView
    private lateinit var textView5: TextView
    private lateinit var btnTixian: Button

    override fun getLayoutId(): Int = R.layout.activity_ti_xian

    override fun initData() {
        ivBack = findViewById(R.id.iv_back)
        tvTitle = findViewById(R.id.tv_title)
        ivSetting = findViewById(R.id.iv_setting)
        accountZhifubao = findViewById(R.id.account_zhifubao)
        selectBank = findViewById(R.id.select_bank)
        chongzhiText = findViewById(R.id.chongzhi_text)
        dividerView = findViewById(R.id.view)
        inputMoney = findViewById(R.id.input_money)
        chongzhiText2 = findViewById(R.id.chongzhi_text2)
        textView5 = findViewById(R.id.textView5)
        btnTixian = findViewById(R.id.btn_tixian)

        ivBack.visibility = View.VISIBLE
        tvTitle.text = "提现"
        ivSetting.visibility = View.GONE

        ivBack.setOnClickListener { removeCurrentActivity() }
        btnTixian.setOnClickListener { withdraw() }

        btnTixian.isClickable = false
        inputMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val moneyNum = inputMoney.text.toString().trim()
                if (TextUtils.isEmpty(moneyNum)) {
                    btnTixian.setBackgroundResource(R.drawable.btn_023)
                    btnTixian.isClickable = false
                } else {
                    btnTixian.setBackgroundResource(R.drawable.btn_01)
                    btnTixian.isClickable = true
                }
            }
        })
    }

    private fun withdraw() {
        Toast.makeText(this, "您的提现请求发送成功,请您稍后查询到账信息", Toast.LENGTH_SHORT).show()
        UIUtils.getHandler().postDelayed({ removeCurrentActivity() }, 2000)
    }
}
