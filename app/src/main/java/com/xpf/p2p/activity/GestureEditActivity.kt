package com.xpf.p2p.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.widget.GestureContentView
import com.xpf.p2p.widget.GestureDrawline
import com.xpf.p2p.widget.LockIndicator

/**
 * Created by xpf on 2016/11/11 :)
 * Function:手势解锁页面
 */
class GestureEditActivity : Activity() {

    private lateinit var mTextTitle: TextView
    private lateinit var mTextCancel: TextView
    private lateinit var mLockIndicator: LockIndicator
    private lateinit var mTextTip: TextView
    private lateinit var mGestureContainer: FrameLayout
    private lateinit var mGestureContentView: GestureContentView
    private lateinit var mTextReset: TextView
    private var mIsFirstInput = true
    private var mFirstPassword: String? = null

    private lateinit var mSharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_edit)
        setUpViews()
    }

    private fun setUpViews() {
        mTextTitle = findViewById(R.id.text_title)
        mTextCancel = findViewById(R.id.text_cancel)
        mTextReset = findViewById(R.id.text_reset)
        mTextReset.isClickable = false
        mLockIndicator = findViewById(R.id.lock_indicator)
        mTextTip = findViewById(R.id.text_tip)
        mGestureContainer = findViewById(R.id.gesture_container)
        mSharedPreferences = getSharedPreferences("secret_protect", Context.MODE_PRIVATE)

        mTextCancel.setOnClickListener { finish() }
        mTextReset.setOnClickListener {
            mIsFirstInput = true
            updateCodeList("")
            mTextTip.text = getString(R.string.set_gesture_pattern)
        }

        mGestureContentView = GestureContentView(this, false, "", object : GestureDrawline.GestureCallBack {
            override fun onGestureCodeInput(inputCode: String) {
                if (!isInputPassValidate(inputCode)) {
                    @Suppress("DEPRECATION")
                    mTextTip.text = Html.fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>")
                    mGestureContentView.clearDrawlineState(0L)
                    return
                }

                if (mIsFirstInput) {
                    mFirstPassword = inputCode
                    updateCodeList(inputCode)
                    mGestureContentView.clearDrawlineState(0L)
                    mTextReset.isClickable = true
                    mTextReset.text = getString(R.string.reset_gesture_code)
                } else {
                    if (inputCode == mFirstPassword) {
                        Toast.makeText(this@GestureEditActivity, "设置成功", Toast.LENGTH_SHORT).show()
                        mGestureContentView.clearDrawlineState(0L)
                        finish()
                    } else {
                        @Suppress("DEPRECATION")
                        mTextTip.text = Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>")
                        val shakeAnimation = android.view.animation.AnimationUtils.loadAnimation(
                            this@GestureEditActivity, R.anim.shake
                        )
                        mTextTip.startAnimation(shakeAnimation)
                        mGestureContentView.clearDrawlineState(1300L)
                    }
                }

                mIsFirstInput = false
            }

            override fun checkedSuccess() {}

            override fun checkedFail() {}
        })

        mGestureContentView.setParentView(mGestureContainer)
        updateCodeList("")
    }

    private fun updateCodeList(inputCode: String) {
        mLockIndicator.setPath(inputCode)
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
