package com.xpf.p2p.ui.login.presenter;

import android.text.TextUtils;

import com.xpf.p2p.App;
import com.xpf.p2p.base.MvpBasePresenter;
import com.xpf.p2p.ui.login.contract.LoginContract;
import com.xpf.p2p.ui.login.listener.OnSmsSendListener;
import com.xpf.p2p.ui.login.listener.OnSmsVerifyListener;
import com.xpf.p2p.ui.login.model.LoginModel;
import com.xpf.p2p.utils.LogUtils;
import com.xpf.p2p.utils.ToastUtil;
import com.xpf.p2p.utils.UIUtils;
import com.xpf.p2p.utils.Validator;

/**
 * Created by x-sir on 2018/9/2 :)
 * Function:
 */
public class LoginPresenter<T extends LoginContract.IView>
        extends MvpBasePresenter<T> implements LoginContract.IPresenter {

    private static final String TAG = "LoginPresenter";
    private LoginContract.IModel mModel = new LoginModel();

    public LoginPresenter() {
    }

    @Override
    public void sendSmsCode() {
        String phone = "";
        if (isNonNull()) {
            phone = getView().getPhoneNumber();
        }

        if (!TextUtils.isEmpty(phone)) {
            if ((phone.length() == 11) && Validator.isChinaPhoneLegal(phone)) {
                if (isNonNull()) {
                    getView().setCountDownView();
                }
                mModel.sendSmsCode(phone, new OnSmsSendListener() {
                    @Override
                    public void onSuccess() {
                        UIUtils.runOnUiThread(() -> ToastUtil.show(App.Companion.getContext(), "发送成功~"));
                    }

                    @Override
                    public void onFailed() {
                        UIUtils.runOnUiThread(() -> ToastUtil.show(App.Companion.getContext(), "发送失败!"));
                    }
                });

            } else {
                ToastUtil.show(App.Companion.getContext(), "手机号码非法！");
            }
        } else {
            ToastUtil.show(App.Companion.getContext(), "请输入手机号码！");
        }
    }

    @Override
    public void verifySmsCode() {
        String phone = "";
        String code = "";
        if (isNonNull()) {
            phone = getView().getPhoneNumber();
            code = getView().getSmsCode();
        }

        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(code)) {
            if ((phone.length() == 11) && Validator.isChinaPhoneLegal(phone)) {
                mModel.verifySmsCode(phone, code, new OnSmsVerifyListener() {
                    @Override
                    public void onCorrect() {
                        LogUtils.i(TAG, "验证码正确~");
                        UIUtils.runOnUiThread(() -> {
                            ToastUtil.show(App.Companion.getContext(), "登录成功~");
                            if (isNonNull()) {
                                getView().loginSuccess();
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        LogUtils.e(TAG, "验证码不正确!");
                        UIUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(App.Companion.getContext(), "验证码不正确！");
                            }
                        });
                    }
                });

            } else {
                ToastUtil.show(App.Companion.getContext(), "手机号码非法！");
            }
        } else {
            ToastUtil.show(App.Companion.getContext(), "手机号或验证码不能为空！");
        }
    }

}
