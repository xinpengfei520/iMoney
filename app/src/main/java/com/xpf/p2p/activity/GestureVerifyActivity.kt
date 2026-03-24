package com.xpf.p2p.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.databinding.ActivityGestureVerifyBinding
import com.xpf.p2p.widget.GestureContentView
import com.xpf.p2p.widget.GestureDrawline

/**
 * Created by xpf on 2016/11/11 :)
 * Function:手势解锁页面
 */
class GestureVerifyActivity : Activity() {

    private lateinit var binding: ActivityGestureVerifyBinding
    private lateinit var mGestureContentView: GestureContentView
    private lateinit var mSharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestureVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        obtainExtraData()
        setUpViews()
    }

    private fun obtainExtraData() {
        mSharedPreferences = getSharedPreferences("secret_protect", Context.MODE_PRIVATE)
    }

    private fun setUpViews() {
        binding.textCancel.setOnClickListener { finish() }

        val inputCode = mSharedPreferences.getString("inputCode", "1235789") ?: "1235789"
        mGestureContentView = GestureContentView(this, true, inputCode,
            object : GestureDrawline.GestureCallBack {
                override fun onGestureCodeInput(inputCode: String) {}

                override fun checkedSuccess() {
                    mGestureContentView.clearDrawlineState(0L)
                    Toast.makeText(this@GestureVerifyActivity, "密码正确", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun checkedFail() {
                    mGestureContentView.clearDrawlineState(1300L)
                    binding.textTip.visibility = View.VISIBLE
                    @Suppress("DEPRECATION")
                    binding.textTip.text = Html.fromHtml("<font color='#c70c1e'>密码错误</font>")
                    val shakeAnimation = AnimationUtils.loadAnimation(this@GestureVerifyActivity, R.anim.shake)
                    binding.textTip.startAnimation(shakeAnimation)
                }
            })

        mGestureContentView.setParentView(binding.gestureContainer)
    }

    private fun getProtectedMobile(phoneNumber: String?): String {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber!!.length < 11) {
            return ""
        }
        return "${phoneNumber.subSequence(0, 3)}****${phoneNumber.subSequence(7, 11)}"
    }

    companion object {
        const val PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER"
        const val PARAM_INTENT_CODE = "PARAM_INTENT_CODE"
    }
}
