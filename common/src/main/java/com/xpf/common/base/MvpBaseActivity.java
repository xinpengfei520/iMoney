package com.xpf.common.base;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by xpf on 2017/7/7 :)
 * Function:Activity的基类
 */

public abstract class MvpBaseActivity<V, T extends MvpBasePresenter<V>> extends Activity {

    public T mPresenter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持手机屏幕常亮
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        bindView();
    }

    /**
     * create presenter with subclass to implementation.
     */
    protected abstract T createPresenter();

    /**
     * 绑定视图让子Activity去实现
     */
    protected abstract void bindView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
