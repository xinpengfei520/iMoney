package com.xpf.p2p.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.databinding.ActivityGestureEditBinding
import com.xpf.p2p.widget.GestureContentView
import com.xpf.p2p.widget.GestureDrawline

/**
 * Created by xpf on 2016/11/11 :)
 * Function:手势解锁页面
 */
class GestureEditActivity : Activity() {

    private lateinit var binding: ActivityGestureEditBinding
    private lateinit var mGestureContentView: GestureContentView
    private var mIsFirstInput = true
    private var mFirstPassword: String? = null

    private lateinit var mSharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestureEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
    }

    private fun setUpViews() {
        binding.textReset.isClickable = false
        mSharedPreferences = getSharedPreferences("secret_protect", Context.MODE_PRIVATE)

        binding.textCancel.setOnClickListener { finish() }
        binding.textReset.setOnClickListener {
            mIsFirstInput = true
            updateCodeList("")
            binding.textTip.text = getString(R.string.set_gesture_pattern)
        }

        mGestureContentView = GestureContentView(this, false, "", object : GestureDrawline.GestureCallBack {
            override fun onGestureCodeInput(inputCode: String) {
                if (!isInputPassValidate(inputCode)) {
                    @Suppress("DEPRECATION")
                    binding.textTip.text = Html.fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>")
                    mGestureContentView.clearDrawlineState(0L)
                    return
                }

                if (mIsFirstInput) {
                    mFirstPassword = inputCode
                    updateCodeList(inputCode)
                    mGestureContentView.clearDrawlineState(0L)
                    binding.textReset.isClickable = true
                    binding.textReset.text = getString(R.string.reset_gesture_code)
                } else {
                    if (inputCode == mFirstPassword) {
                        Toast.makeText(this@GestureEditActivity, "设置成功", Toast.LENGTH_SHORT).show()
                        mGestureContentView.clearDrawlineState(0L)
                        finish()
                    } else {
                        @Suppress("DEPRECATION")
                        binding.textTip.text = Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>")
                        val shakeAnimation = android.view.animation.AnimationUtils.loadAnimation(
                            this@GestureEditActivity, R.anim.shake
                        )
                        binding.textTip.startAnimation(shakeAnimation)
                        mGestureContentView.clearDrawlineState(1300L)
                    }
                }

                mIsFirstInput = false
            }

            override fun checkedSuccess() {}

            override fun checkedFail() {}
        })

        mGestureContentView.setParentView(binding.gestureContainer)
        updateCodeList("")
    }

    private fun updateCodeList(inputCode: String) {
        binding.lockIndicator.setPath(inputCode)
        mSharedPreferences.edit().putString("inputCode", inputCode).apply()
        Log.e("TAG", "inputCode = $inputCode")
    }

    private fun isInputPassValidate(inputPassword: String): Boolean {
        return !TextUtils.isEmpty(inputPassword) && inputPassword.length >= 4
    }

    companion object {
        const val PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER"
        const val PARAM_INTENT_CODE = "PARAM_INTENT_CODE"
        const val PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE"
    }
}
