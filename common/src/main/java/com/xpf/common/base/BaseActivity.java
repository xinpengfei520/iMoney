package com.xpf.common.base;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xpf.common.manager.ActivityManager;

import butterknife.ButterKnife;

/**
 * Created by xpf on 2016/11/16 :)
 * Wechat:vancexin
 * Function:提供通用的Activity的使用的基类,让MainActivity和LoginActivity继承它
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ButterKnife.bind(this);
        // 将当前的Activity添加到ActivityManager中
        ActivityManager.getInstance().add(this);
        initData();
    }

    /**
     * 初始化内容数据
     */
    protected abstract void initData();

    /**
     * 提供加载的布局的方法(暴露出去,强制让子类去实现)
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 销毁当前的activity
     */
    public void removeCurrentActivity() {
        ActivityManager.getInstance().removeCurrent();
    }

    /**
     * 启动新的activity
     *
     * @param activity
     * @param bundle
     */
    public void goToActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    /**
     * 销毁所有的Activity
     */
    public void removeAll() {
        ActivityManager.getInstance().removeAll();
    }

}
