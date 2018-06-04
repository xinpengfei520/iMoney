package com.xpf.p2p.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xpf.common.base.BaseActivity;
import com.xpf.common.bean.User;
import com.xpf.common.cons.ApiRequestUrl;
import com.xpf.p2p.R;
import com.xpf.p2p.utils.MD5Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.log_ed_mob)
    EditText logEdMob;
    @BindView(R.id.tvTestUse)
    TextView tvTestUse;
    @BindView(R.id.log_ed_pad)
    EditText logEdPad;
    @BindView(R.id.log_log_btn)
    Button logLogBtn;

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.log_log_btn, R.id.tvTestUse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_log_btn:
                login();
                break;
            case R.id.tvTestUse:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    private void login() {
        // 1.获取手机号和加密以后的密码
        String number = logEdMob.getText().toString();
        String password = logEdPad.getText().toString();
        // 判断输入的信息是否存在空
        if (!TextUtils.isEmpty(number) && !TextUtils.isEmpty(password)) {
            // 2.联网将用户数据发送给服务器，其中手机号和密码作为请求参数
            String url = ApiRequestUrl.LOGIN;
            RequestParams params = new RequestParams();
            params.put("username", number);
            params.put("password", MD5Utils.MD5(password));
            client.post(url, params, new AsyncHttpResponseHandler() {

                // 3.得到响应数据成功
                @Override
                public void onSuccess(String content) {
                    // 3.1解析Json数据
                    JSONObject jsonObject = JSON.parseObject(content);
                    boolean isSuccess = jsonObject.getBoolean("success");
                    if (isSuccess) {
                        String data = jsonObject.getString("data");
                        User user = JSON.parseObject(data, User.class);
                        //3.2sp保存得到的用户信息
                        saveUser(user);
                        //3.3重新加载页面,显示用户的信息在MeFragment中
                        LoginActivity.this.removeAll();
                        LoginActivity.this.goToActivity(MainActivity.class, null);
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名不存在或密码不正确", Toast.LENGTH_SHORT).show();
                    }

                }

                // 4.连接失败
                @Override
                public void onFailure(Throwable error, String content) {
                    Toast.makeText(LoginActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }

}
