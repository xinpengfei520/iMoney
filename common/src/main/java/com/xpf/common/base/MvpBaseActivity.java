package com.xpf.common.base;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xpf.common.manager.ActivityManager;

import butterknife.ButterKnife;

/**
 * Created by xpf on 2017/7/7 :)
 * Function:Activity的基类
 */
public abstract class MvpBaseActivity<V, T extends MvpBasePresenter<V>> extends AppCompatActivity {

    public T mPresenter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        setContentView(getLayoutId());
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ButterKnife.bind(this);
        ActivityManager.getInstance().add(this);
        initData();
    }

    /**
     * create presenter with subclass to implementation.
     */
    protected abstract T createPresenter();

    /**
     * 绑定视图让子Activity去实现
     */
    protected abstract int getLayoutId();

    /**
     * 初始化相关数据
     */
    protected abstract void initData();

    /**
     * 销毁当前的 activity，等同于 finish()
     */
    public void removeCurrentActivity() {
        ActivityManager.getInstance().removeCurrent();
    }

    /**
     * 启动新的 Activity
     *
     * @param activity aim activity
     * @param bundle   pass bundle data if need.
     */
    protected void goToActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    /**
     * 销毁所有的 Activity，即退出 APP
     */
    protected void removeAll() {
        ActivityManager.getInstance().removeAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
