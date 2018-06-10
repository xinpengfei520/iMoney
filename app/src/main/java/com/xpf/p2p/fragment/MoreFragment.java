package com.xpf.p2p.fragment;

import android.content.Intent;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.xpf.common.base.BaseFragment;
import com.xpf.p2p.R;
import com.xpf.p2p.multilanguage.view.MultiLanguageActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xpf on 2016/11/11 :)
 * Wechat:18091383534
 * Function:更多页面(跑马灯效果)
 */

public class MoreFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;

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
        /*// 方式一:
        tvContent.setFocusable(true); // 获取焦点
        tvContent.setFocusableInTouchMode(true);
        tvContent.requestFocus();
        // 方式二:提供TextView的子视图并重写isFocus()方法*/
    }

    @OnClick(R.id.tvLanguage)
    public void onViewClicked() {
        startActivity(new Intent(mContext, MultiLanguageActivity.class));
    }
}
