package com.xpf.p2p.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.xpf.common.base.BaseFragment;
import com.xpf.p2p.R;
import com.xpf.p2p.ui.multilanguage.view.MultiLanguageActivity;


/**
 * Created by xpf on 2016/11/11 :)
 * Function:更多页面(跑马灯效果)
 */

public class MoreFragment extends BaseFragment {

    TextView tvContent;
    TextView tvLanguage;
    WebView webView;
    ProgressBar progressBar;
    private static final String TAG = "MoreFragment";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) {
        tvContent = mView.findViewById(R.id.tv_content);
        tvLanguage = mView.findViewById(R.id.tvLanguage);
        webView = mView.findViewById(R.id.webView);
        progressBar = mView.findViewById(R.id.progressBar);
        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MultiLanguageActivity.class));
            }
        });

        /*// 方式一:
        tvContent.setFocusable(true); // 获取焦点
        tvContent.setFocusableInTouchMode(true);
        tvContent.requestFocus();
        // 方式二:提供TextView的子视图并重写isFocus()方法*/
        initData();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 解除数据阻止
        webView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("https://github.com/xinpengfei520/iMoney");
    }
}
