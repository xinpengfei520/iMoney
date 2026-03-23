package com.xpf.p2p.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import com.xpf.p2p.R
import com.xpf.p2p.base.BaseFragment
import com.xpf.p2p.ui.multilanguage.view.MultiLanguageActivity

/**
 * Created by xpf on 2016/11/11 :)
 * Function:更多页面(跑马灯效果)
 */
class MoreFragment : BaseFragment() {

    private lateinit var tvContent: TextView
    private lateinit var tvLanguage: TextView
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun getLayoutId(): Int = R.layout.fragment_more

    override fun getUrl(): String = ""

    override fun getParams(): Map<String, String>? = null

    override fun initData(content: String?) {
        tvContent = mView!!.findViewById(R.id.tv_content)
        tvLanguage = mView!!.findViewById(R.id.tvLanguage)
        webView = mView!!.findViewById(R.id.webView)
        progressBar = mView!!.findViewById(R.id.progressBar)
        tvLanguage.setOnClickListener {
            startActivity(Intent(mContext, MultiLanguageActivity::class.java))
        }

        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.domStorageEnabled = true
        webView.settings.blockNetworkImage = false
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        webView.loadUrl("https://github.com/xinpengfei520/iMoney")
    }
}
