package com.xpf.p2p.base;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.RequestParams;
import com.xpf.p2p.constants.ResultState;
import com.xpf.p2p.utils.UIUtils;
import com.xpf.p2p.widget.LoadingPage;

/**
 * Created by xpf on 2016/11/14 :)
 * Wechat:vancexin
 * Function:各个Fragment类的基类,其他都要继承它
 */

public abstract class BaseFragment extends Fragment {

    public LoadingPage loadingPage;
    protected Context mContext;
    protected View mView;

    // 返回一个关联的布局文件生成的视图
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 方式一:使用Application的实例作为Context的对象
        mView = UIUtils.getView(getLayoutId());
        mContext = getActivity();
        String url = getUrl();
        if (TextUtils.isEmpty(url)) {
            // 初始化页面数据
            initData(null);
            return mView;
        } else {
            loadingPage = new LoadingPage(getActivity()) {

                @Override
                public int layoutId() {
                    return getLayoutId();
                }

                @Override
                protected void onSuccess(ResultState resultState, View view_success) {
                    //ButterKnife.bind(BaseFragment.this, view_success); // 别忘了绑定布局
                    initData(resultState.getContent());
                }

                @Override
                protected RequestParams params() {
                    return getParams();
                }

                @Override
                protected String url() {
                    return getUrl();
                }
            };

            return loadingPage;
        }
    }

    public abstract int getLayoutId();

    protected abstract String getUrl();

    protected abstract RequestParams getParams();

    protected abstract void initData(String content);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = getUrl();
        if (!TextUtils.isEmpty(url)) {
            show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // 实现联网操作
    public void show() {
        /*UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingPage.show();
            }
        },2000);*/
        loadingPage.show();
    }
}
