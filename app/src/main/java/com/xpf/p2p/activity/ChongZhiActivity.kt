package com.xpf.p2p.activity

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseActivity
import com.xpf.p2p.utils.pay.PayKeys
import com.xpf.p2p.utils.pay.PayResult
import com.xpf.p2p.utils.pay.SignUtils
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

/**
 * Created by Vance on 2016/8/3 :)
 * Function:用户账户充值页面
 */
class ChongZhiActivity : BaseActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivSetting: ImageView
    private lateinit var chongzhiText: TextView
    private lateinit var chongzhiEt: EditText
    private lateinit var chongzhiText2: TextView
    private lateinit var yueTv: TextView
    private lateinit var chongzhiBtn: Button

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as String)
                    val resultStatus = payResult.resultStatus
                    when {
                        TextUtils.equals(resultStatus, "9000") ->
                            Toast.makeText(this@ChongZhiActivity, "支付成功", Toast.LENGTH_SHORT).show()
                        TextUtils.equals(resultStatus, "8000") ->
                            Toast.makeText(this@ChongZhiActivity, "支付结果确认中", Toast.LENGTH_SHORT).show()
                        else ->
                            Toast.makeText(this@ChongZhiActivity, "支付失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_chong_zhi

    override fun initData() {
        ivBack = findViewById(R.id.iv_back)
        tvTitle = findViewById(R.id.tv_title)
        ivSetting = findViewById(R.id.iv_setting)
        chongzhiText = findViewById(R.id.chongzhi_text)
        chongzhiEt = findViewById(R.id.chongzhi_et)
        chongzhiText2 = findViewById(R.id.chongzhi_text2)
        yueTv = findViewById(R.id.yue_tv)
        chongzhiBtn = findViewById(R.id.chongzhi_btn)

        ivBack.visibility = View.VISIBLE
        tvTitle.text = "充值"
        ivSetting.visibility = View.GONE
        chongzhiBtn.isClickable = false

        ivBack.setOnClickListener { removeCurrentActivity() }
        chongzhiBtn.setOnClickListener { recharge() }

        chongzhiEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.e("TAG", "ChongZhiActivity----> beforeTextChanged()")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("TAG", "ChongZhiActivity----> onTextChanged()")
            }

            override fun afterTextChanged(s: Editable?) {
                Log.e("TAG", "ChongZhiActivity----> afterTextChanged()")
                val moneyNum = chongzhiEt.text.toString().trim()
                if (TextUtils.isEmpty(moneyNum)) {
                    chongzhiBtn.setBackgroundResource(R.drawable.btn_023)
                    chongzhiBtn.isClickable = false
                } else {
                    chongzhiBtn.setBackgroundResource(R.drawable.btn_01)
                    chongzhiBtn.isClickable = true
                }
            }
        })
    }

    private fun recharge() {
        val orderInfo = getOrderInfo("iphone 7 plus 256G", "史上最强配置iphone", "0.01")
        var sign = sign(orderInfo)
        try {
            sign = URLEncoder.encode(sign, "UTF-8")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val payInfo = "$orderInfo&sign=\"$sign\"&${getSignType()}"

        Thread {
            val alipay = PayTask(this@ChongZhiActivity)
            val result = alipay.pay(payInfo)
            val msg = Message.obtain().apply {
                what = SDK_PAY_FLAG
                obj = result
            }
            mHandler.sendMessage(msg)
        }.start()
    }

    fun getSignType(): String = "sign_type=\"RSA\""

    fun sign(content: String): String = SignUtils.sign(content, RSA_PRIVATE)

    fun getOrderInfo(subject: String, body: String, price: String): String {
        var orderInfo = "partner=\"$PARTNER\""
        orderInfo += "&seller_id=\"$SELLER\""
        orderInfo += "&out_trade_no=\"${getOutTradeNo()}\""
        orderInfo += "&subject=\"$subject\""
        orderInfo += "&body=\"$body\""
        orderInfo += "&total_fee=\"$price\""
        orderInfo += "&notify_url=\"http://notify.msp.hk/notify.htm\""
        orderInfo += "&service=\"mobile.securitypay.pay\""
        orderInfo += "&payment_type=\"1\""
        orderInfo += "&_input_charset=\"utf-8\""
        orderInfo += "&it_b_pay=\"30m\""
        orderInfo += "&return_url=\"m.alipay.com\""
        return orderInfo
    }

    fun getOutTradeNo(): String {
        val format = SimpleDateFormat("MMddHHmmss", Locale.getDefault())
        var key = format.format(Date())
        val r = Random()
        key = key + r.nextInt()
        return key.substring(0, 15)
    }

    companion object {
        private const val TAG = "ChongZhiActivity"
        private const val PARTNER = PayKeys.DEFAULT_PARTNER
        private const val SELLER = PayKeys.DEFAULT_SELLER
        private const val RSA_PRIVATE = PayKeys.PRIVATE
        private const val SDK_PAY_FLAG = 1
    }
}
