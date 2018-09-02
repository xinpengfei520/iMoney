package com.xpf.p2p.ui.multilanguage.contract;

import com.xpf.p2p.multilanguage.listener.OnLanguageListener;
import com.xpf.p2p.multilanguage.listener.OnLanguageSaveListener;

/**
 * Created by xpf on 2018/6/9 :)
 * GitHub:xinpengfei520
 * Function:
 */
public interface MultiLanguageContract {

    interface IModel {
        void getLanguage(OnLanguageListener listener);

        void saveSetting(int language, OnLanguageSaveListener listener);
    }

    interface IView {
        void showLanguageSetting(int currentLanguage);

        void showSaveSuccess();
    }

    interface IPresenter {
        void getLanguageSetting();

        void saveSetting(int language);
    }
}
