package com.xpf.p2p.ui.login.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xpf.common.base.MvpBaseActivity;
import com.xpf.http.logger.XLog;
import com.xpf.p2p.R;
import com.xpf.p2p.ui.login.contract.LoginContract;
import com.xpf.p2p.ui.login.presenter.LoginPresenter;
import com.xpf.p2p.ui.main.view.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.smssdk.SMSSDK;

/**
 * Created by x-sir on 2016/9/2 :)
 * Function:登录页面
 */
public class LoginActivity extends MvpBaseActivity<LoginContract.IView,
        LoginPresenter<LoginContract.IView>> implements LoginContract.IView {

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
    protected LoginPresenter<LoginContract.IView> createPresenter() {
        return new LoginPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

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
                // 小细节，当用户输入完 11 位手机号码时，才显示发送验证码按钮
                tvSendSmsCode.setVisibility(charSequence.length() != 11 ? View.GONE : View.VISIBLE);
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
    public String getPhoneNumber() {
        return logEdMob.getText().toString();
    }

    @Override
    public String getSmsCode() {
        return logEdPad.getText().toString();
    }

    @Override
    public void setCountDownView() {
        tvSendSmsCode.setVisibility(View.GONE);
        countDownView.setVisibility(View.VISIBLE);
        countDownView.start(60000);
    }

    @Override
    public void loginSuccess() {
        goToActivity(MainActivity.class, null);
        removeCurrentActivity();
    }

    @OnClick({R.id.log_log_btn, R.id.tvTestUse, R.id.tvSendSmsCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_log_btn:
                mPresenter.verifySmsCode();
                break;
            case R.id.tvTestUse:
                loginSuccess();
                break;
            case R.id.tvSendSmsCode:
                mPresenter.sendSmsCode();
                break;
        }
    }

    public static void actionStart(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            XLog.e("context is null!");
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // 用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

//    private void login() {
//        // 1.获取手机号和加密以后的密码
//        String number = logEdMob.getText().toString();
//        String password = logEdPad.getText().toString();
//        // 判断输入的信息是否存在空
//        if (!TextUtils.isEmpty(number) && !TextUtils.isEmpty(password)) {
//            // 2.联网将用户数据发送给服务器，其中手机号和密码作为请求参数
//            String url = ApiRequestUrl.LOGIN;
//            RequestParams params = new RequestParams();
//            params.put("username", number);
//            params.put("password", MD5Utils.MD5(password));
//            client.post(url, params, new AsyncHttpResponseHandler() {
//
//                // 3.得到响应数据成功
//                @Override
//                public void onSuccess(String content) {
//                    // 3.1解析Json数据
//                    JSONObject jsonObject = JSON.parseObject(content);
//                    boolean isSuccess = jsonObject.getBoolean("success");
//                    if (isSuccess) {
//                        String data = jsonObject.getString("data");
//                        User user = JSON.parseObject(data, User.class);
//                        //3.2sp保存得到的用户信息
//                        saveUser(user);
//                        //3.3重新加载页面,显示用户的信息在MeFragment中
//                        LoginActivity.this.removeAll();
//                        LoginActivity.this.goToActivity(MainActivity.class, null);
//                    } else {
//                        Toast.makeText(LoginActivity.this, "用户名不存在或密码不正确", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                // 4.连接失败
//                @Override
//                public void onFailure(Throwable error, String content) {
//                    Toast.makeText(LoginActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
//        }
//    }
}
