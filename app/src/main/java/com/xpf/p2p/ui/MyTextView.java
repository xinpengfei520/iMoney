package com.xpf.p2p.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xpf on 2016/11/19 :)
 * Wechat:18091383534
 * Function:
 */

public class MyTextView extends TextView {

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
