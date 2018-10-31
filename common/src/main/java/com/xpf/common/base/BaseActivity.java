package com.xpf.common.base;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.xpf.common.bean.User;
import com.xpf.common.manager.ActivityManager;

import butterknife.ButterKnife;

/**
 * Created by xpf on 2016/11/16 :)
 * Wechat:18091383534
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

    // 初始化内容数据
    protected abstract void initData();

    // 提供加载的布局的方法(暴露出去,强制让子类去实现)
    protected abstract int getLayoutId();

    // 销毁当前的activity
    public void removeCurrentActivity() {
        ActivityManager.getInstance().removeCurrent();
    }

    // 启动新的activity
    public void goToActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    // 销毁所有的Activity
    public void removeAll() {
        ActivityManager.getInstance().removeAll();
    }

    // 使用AsyncHttpClient,实现联网的声明
    public AsyncHttpClient client = new AsyncHttpClient();

    // 保存用户信息的操作,使用sp存储
    @SuppressLint("ApplySharedPref")
    public void saveUser(User user) {
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("UF_ACC", user.UF_ACC);
        edit.putString("UF_AVATAR_URL", user.UF_AVATAR_URL);
        edit.putString("UF_IS_CERT", user.UF_IS_CERT);
        edit.putString("UF_PHONE", user.UF_PHONE);
        edit.commit(); // 只有提交以后,才可以创建此文件,并保存数据
    }

    // 读取数据,得到内存中的User对象
    public User readUser() {
        User user = new User();
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        user.UF_ACC = sp.getString("UF_ACC", "");
        user.UF_AVATAR_URL = sp.getString("UF_AVATAR_URL", "");
        user.UF_IS_CERT = sp.getString("UF_IS_CERT", "");
        user.UF_PHONE = sp.getString("UF_PHONE", "");
        return user;
    }

}
