package com.xpf.p2p.ui.login.model;

import com.xpf.p2p.App;
import com.xpf.p2p.constants.SpKey;
import com.xpf.p2p.ui.login.contract.LoginContract;
import com.xpf.p2p.ui.login.listener.OnSmsSendListener;
import com.xpf.p2p.ui.login.listener.OnSmsVerifyListener;
import com.xpf.p2p.utils.LogUtils;
import com.xpf.p2p.utils.SpUtil;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by x-sir on 2018/9/2 :)
 * Function:
 */
public class LoginModel implements LoginContract.IModel {

    private static final String TAG = "LoginModel";
    private static final String COUNTRY_CODE = "86";

    public LoginModel() {
    }

    @Override
    public void sendSmsCode(String phone, OnSmsSendListener listener) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    LogUtils.i(TAG, "afterEvent() -> RESULT_COMPLETE");
                    // 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    if (listener != null) {
                        listener.onSuccess();
                    }
                } else {
                    // 处理错误的结果
                    if (listener != null) {
                        listener.onFailed();
                    }
                }
            }
        });

        // 触发操作请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
        SMSSDK.getVerificationCode(COUNTRY_CODE, phone);
    }

    @Override
    public void verifySmsCode(String phone, String code, OnSmsVerifyListener listener) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理验证成功的结果
                    if (listener != null) {
                        listener.onCorrect();
                    }
                    saveLoginState();
                } else {
                    // 处理错误的结果
                    if (listener != null) {
                        listener.onError();
                    }
                }
            }
        });
        // 触发操作，提交验证码，其中的code表示验证码，如“1357”
        SMSSDK.submitVerificationCode(COUNTRY_CODE, phone, code);
    }

    /**
     * 保存登录成功的状态，记录当前登录成功时的时间戳，然后在闪屏页面进行判断
     * 如果小于 7 天，默认直接跳到主页面，否则就需要重新登录，正常开发中登录成功时，
     * 服务器端会给你返回一个 token，这个 token 是有有效期的，此处为了简便，
     * 就采用这种方式来实现，缺点当然就是不安全！
     */
    private void saveLoginState() {
        long millis = System.currentTimeMillis();
        SpUtil.getInstance(App.Companion.getContext()).save(
                SpKey.LOGIN_SUCCESS_TIMESTAMP, String.valueOf(millis));
    }
}
