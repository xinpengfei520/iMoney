package com.xpf.p2p.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wondersgroup.android.jkcs_sdk.R;

/**
 * Created by x-sir on 2018/9/2 :)
 * Function:
 */
public class TitleBarLayout extends LinearLayout {

    private ImageView ivBack;
    private ImageView ivMenu;
    private TextView tvTitleName;
    private LinearLayout llTitleBg;
    private CharSequence mText;
    private int mTitleBgColor;
    private int mTextSize;
    private int mTextColor;
    private static final String DEFAULT_TEXT = "Title"; // default text.
    private static final int DEFAULT_TEXT_SIZE = 16; // default text size.

    public TitleBarLayout(Context context) {
        this(context, null);
    }

    public TitleBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout);
        mText = typedArray.getText(R.styleable.TitleBarLayout_text);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleBarLayout_textSize, DEFAULT_TEXT_SIZE);
        mTextColor = typedArray.getColor(R.styleable.TitleBarLayout_textColor, Color.parseColor("#FFFFFF"));
        mTitleBgColor = typedArray.getColor(R.styleable.TitleBarLayout_titleBgColor, Color.parseColor("#1E90FF"));
        typedArray.recycle();

        initView(context);
        initData();
        initListener();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.wonders_group_titlebar, this);
        ivBack = findViewById(R.id.ivBack);
        ivMenu = findViewById(R.id.ivMenu);
        tvTitleName = findViewById(R.id.tvTitleName);
        llTitleBg = findViewById(R.id.llTitleBg);
    }

    private void initData() {
        String text = (mText != null) ? mText.toString() : DEFAULT_TEXT;
        tvTitleName.setText(text);
        tvTitleName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        tvTitleName.setTextColor(mTextColor);
        llTitleBg.setBackgroundColor(mTitleBgColor);
    }

    private void initListener() {
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        ivMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
