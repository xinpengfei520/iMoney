package com.xpf.p2p.ui.login.contract;

import com.xpf.p2p.ui.login.listener.OnSmsSendListener;
import com.xpf.p2p.ui.login.listener.OnSmsVerifyListener;

/**
 * Created by x-sir on 2018/9/2 :)
 * Function:
 */
public interface LoginContract {

    interface IModel {
        void sendSmsCode(String phone, OnSmsSendListener listener);

        void verifySmsCode(String phone, String code, OnSmsVerifyListener listener);
    }

    interface IView {
        String getPhoneNumber();

        String getSmsCode();

        void setCountDownView();

        void loginSuccess();
    }

    interface IPresenter {
        void sendSmsCode();

        void verifySmsCode();
    }
}
