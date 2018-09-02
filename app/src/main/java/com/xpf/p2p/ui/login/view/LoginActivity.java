package com.xpf.p2p.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.xpf.common.utils.ToastUtil;
import com.xpf.common.utils.Validator;
import com.xpf.p2p.R;
import com.xpf.p2p.utils.MD5Utils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.log_ed_mob)
    EditText logEdMob;
    @BindView(R.id.tvTestUse)
    TextView tvTestUse;
    @BindView(R.id.log_ed_pad)
    EditText logEdPad;
    @BindView(R.id.log_log_btn)
    Button logLogBtn;
    @BindView(R.id.tvSendSmsCode)
    TextView tvSendSmsCode;
    @BindView(R.id.countDownView)
    CountdownView countDownView;

    @Override
    protected void initData() {
        initListener();
    }

    private void initListener() {
        countDownView.setOnCountdownEndListener(cv -> {
            countDownView.setVisibility(View.GONE);
            tvSendSmsCode.setVisibility(View.VISIBLE);
            tvSendSmsCode.setText(R.string.resend);
        });
        logEdMob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvSendSmsCode.setVisibility(charSequence.length() == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        logEdPad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                logLogBtn.setEnabled(charSequence.length() != 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.log_log_btn, R.id.tvTestUse, R.id.tvSendSmsCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_log_btn:
                //login();
                submitCode("86", logEdMob.getText().toString(), logEdPad.getText().toString());
                break;
            case R.id.tvTestUse:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.tvSendSmsCode:
                String phone = logEdMob.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    if ((phone.length() == 11) && Validator.isChinaPhoneLegal(phone)) {
                        tvSendSmsCode.setVisibility(View.GONE);
                        countDownView.setVisibility(View.VISIBLE);
                        countDownView.start(60000);
                        sendCode("86", phone);
                    } else {
                        ToastUtil.show(this, "手机号码非法！");
                    }
                } else {
                    ToastUtil.show(this, "请输入手机号码！");
                }
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

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    private void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    runOnUiThread(() -> ToastUtil.show(LoginActivity.this, "发送成功~"));
                } else {
                    // TODO 处理错误的结果
                    runOnUiThread(() -> ToastUtil.show(LoginActivity.this, "发送失败!"));
                }
            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    private void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    runOnUiThread(() -> ToastUtil.show(LoginActivity.this, "验证码正确~"));
                } else {
                    // TODO 处理错误的结果
                    runOnUiThread(() -> ToastUtil.show(LoginActivity.this, "验证码不正确！"));
                }
            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }
}
