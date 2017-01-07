package com.xpf.p2p.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xpf.p2p.ui.LoadingPage;
import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;

/**
 * Created by xpf on 2016/11/14 :)
 * Wechat:18091383534
 * Function:各个Fragment类的基类,其他都要继承它
 */

public abstract class BaseFragment extends Fragment {

    public LoadingPage loadingPage;

    // 返回一个关联的布局文件生成的视图
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//         方式一:使用Application的实例作为Context的对象
//        View view = UIUtils.getView(getLayoutId());
//        ButterKnife.bind(this, view);
//
//         初始化页面数据
//        initData();
        loadingPage = new LoadingPage(getActivity()) {

            @Override
            public int layoutId() {
                return getLayoutId();
            }

            @Override
            protected void onSuccess(ResultState resultState, View view_success) {
                ButterKnife.bind(BaseFragment.this, view_success); // 别忘了绑定布局

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

    public abstract int getLayoutId();

    protected abstract String getUrl();

    protected abstract RequestParams getParams();

    protected abstract void initData(String content);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //实现联网操作
    public void show() {

//        UIUtils.getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadingPage.show();
//            }
//        },2000);
        loadingPage.show();
    }
}
