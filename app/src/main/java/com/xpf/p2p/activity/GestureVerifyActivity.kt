package com.xpf.p2p.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.widget.GestureContentView
import com.xpf.p2p.widget.GestureDrawline

/**
 * Created by xpf on 2016/11/11 :)
 * Function:手势解锁页面
 */
class GestureVerifyActivity : Activity() {

    private lateinit var mTopLayout: RelativeLayout
    private lateinit var mTextTitle: TextView
    private lateinit var mTextCancel: TextView
    private lateinit var mImgUserLogo: ImageView
    private lateinit var mTextPhoneNumber: TextView
    private lateinit var mTextTip: TextView
    private lateinit var mGestureContainer: FrameLayout
    private lateinit var mGestureContentView: GestureContentView
    private lateinit var mTextForget: TextView
    private lateinit var mTextOther: TextView
    private lateinit var mSharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_verify)
        obtainExtraData()
        setUpViews()
    }

    private fun obtainExtraData() {
        mSharedPreferences = getSharedPreferences("secret_protect", Context.MODE_PRIVATE)
    }

    private fun setUpViews() {
        mTopLayout = findViewById(R.id.top_layout)
        mTextTitle = findViewById(R.id.text_title)
        mTextCancel = findViewById(R.id.text_cancel)
        mImgUserLogo = findViewById(R.id.user_logo)
        mTextPhoneNumber = findViewById(R.id.text_phone_number)
        mTextTip = findViewById(R.id.text_tip)
        mGestureContainer = findViewById(R.id.gesture_container)
        mTextForget = findViewById(R.id.text_forget_gesture)
        mTextOther = findViewById(R.id.text_other_account)
        mTextCancel.setOnClickListener { finish() }

        val inputCode = mSharedPreferences.getString("inputCode", "1235789")
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
                    mTextTip.visibility = View.VISIBLE
                    @Suppress("DEPRECATION")
                    mTextTip.text = Html.fromHtml("<font color='#c70c1e'>密码错误</font>")
                    val shakeAnimation = AnimationUtils.loadAnimation(this@GestureVerifyActivity, R.anim.shake)
                    mTextTip.startAnimation(shakeAnimation)
                }
            })

        mGestureContentView.setParentView(mGestureContainer)
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
