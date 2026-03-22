package com.xpf.p2p.jpush

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import cn.jpush.android.api.JPushInterface

class TestActivity : Activity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "用户自定义打开的Activity"
        val intent = intent
        if (intent != null) {
            val bundle = intent.extras
            var title: String? = null
            var content: String? = null
            if (bundle != null) {
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE)
                content = bundle.getString(JPushInterface.EXTRA_ALERT)
            }
            tv.text = "Title : $title  Content : $content"
        }
        addContentView(tv, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }
}
