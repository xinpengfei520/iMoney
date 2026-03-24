package com.xpf.p2p.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.ui.multilanguage.view.MultiLanguageActivity
import com.xpf.p2p.utils.AppUtil

/**
 * Created by xpf on 2016/11/11 :)
 * Function:更多页面
 */
class MoreFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_more

    override fun getUrl(): String = ""

    override fun getParams(): Map<String, String>? = null

    @SuppressLint("SetTextI18n")
    override fun initData(content: String?) {
        val view = mView ?: return

        // 版本号
        val tvVersion = view.findViewById<TextView>(R.id.tv_version)
        tvVersion.text = "V${AppUtil.getAppVersionName(activity!!)}"

        // 多语言
        view.findViewById<LinearLayout>(R.id.item_language).setOnClickListener {
            startActivity(Intent(mContext, MultiLanguageActivity::class.java))
        }

        // 消息通知
        view.findViewById<LinearLayout>(R.id.item_notification).setOnClickListener {
            Toast.makeText(activity, "敬请期待", Toast.LENGTH_SHORT).show()
        }

        // 安全中心
        view.findViewById<LinearLayout>(R.id.item_security).setOnClickListener {
            Toast.makeText(activity, "敬请期待", Toast.LENGTH_SHORT).show()
        }

        // 清除缓存
        view.findViewById<LinearLayout>(R.id.item_clear_cache).setOnClickListener {
            Toast.makeText(activity, "缓存已清除", Toast.LENGTH_SHORT).show()
        }

        // 检查更新
        view.findViewById<LinearLayout>(R.id.item_check_update).setOnClickListener {
            Toast.makeText(activity, "当前已是最新版本", Toast.LENGTH_SHORT).show()
        }

        // 关于我们
        view.findViewById<LinearLayout>(R.id.item_about).setOnClickListener {
            Toast.makeText(activity, "敬请期待", Toast.LENGTH_SHORT).show()
        }

        // GitHub
        view.findViewById<LinearLayout>(R.id.item_github).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/xinpengfei520/iMoney"))
            startActivity(intent)
        }
    }
}
