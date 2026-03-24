package com.xpf.p2p.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.xpf.p2p.R
import com.xpf.p2p.utils.StatusBarUtils

/**
 * Created by x-sir on 2018/9/2 :)
 * Function:自定义通用 TitleBar
 */
class TitleBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val ivBack: ImageView
    private val ivMenu: ImageView
    private val tvTitleName: TextView
    private val llTitleBg: LinearLayout
    private val mText: CharSequence?
    private val mTitleBgColor: Int
    private val mMenuVisible: Boolean
    private val mTextSize: Int
    private val mTextColor: Int
    private var mListener: OnMenuClickListener? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout)
        mText = typedArray.getText(R.styleable.TitleBarLayout_text)
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBarLayout_android_textSize, DEFAULT_TEXT_SIZE)
        mTextColor = typedArray.getColor(R.styleable.TitleBarLayout_android_textColor, Color.parseColor("#FFFFFF"))
        mTitleBgColor = typedArray.getColor(R.styleable.TitleBarLayout_titleBgColor, Color.parseColor("#1E90FF"))
        mMenuVisible = typedArray.getBoolean(R.styleable.TitleBarLayout_menuVisible, false)
        typedArray.recycle()

        LayoutInflater.from(context).inflate(R.layout.common_titlebar, this)
        ivBack = findViewById(R.id.ivBack)
        ivMenu = findViewById(R.id.ivMenu)
        tvTitleName = findViewById(R.id.tvTitleName)
        llTitleBg = findViewById(R.id.llTitleBg)

        val text = mText?.toString() ?: DEFAULT_TEXT
        tvTitleName.text = text
        tvTitleName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
        tvTitleName.setTextColor(mTextColor)
        llTitleBg.setBackgroundColor(mTitleBgColor)
        ivMenu.visibility = if (mMenuVisible) VISIBLE else INVISIBLE

        // 设置状态栏占位高度
        val statusBarSpace = findViewById<View>(R.id.statusBarSpace)
        if (context is Activity) {
            statusBarSpace.layoutParams.height = StatusBarUtils.getStatusBarHeight(context as Activity)
        }

        ivBack.setOnClickListener { (context as Activity).finish() }
        ivMenu.setOnClickListener { mListener?.onClick() }
    }

    fun setOnMenuListener(mListener: OnMenuClickListener?) {
        this.mListener = mListener
    }

    interface OnMenuClickListener {
        fun onClick()
    }

    companion object {
        private const val DEFAULT_TEXT = "Title"
        private const val DEFAULT_TEXT_SIZE = 16
    }
}
