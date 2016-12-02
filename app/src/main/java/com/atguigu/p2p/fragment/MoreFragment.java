package com.atguigu.p2p.fragment;

import android.widget.TextView;

import com.atguigu.p2p.R;
import com.atguigu.p2p.common.BaseFragment;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;

/**
 * Created by xpf on 2016/11/11 :)
 * Wechat:18091383534
 * Function:更多页面(跑马灯效果)
 */

public class MoreFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;

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

        // 方式一:
//        tvContent.setFocusable(true); // 获取焦点
//        tvContent.setFocusableInTouchMode(true);
//        tvContent.requestFocus();
        // 方式二:提供TextView的子视图并重写isFocus()方法
    }

}
