package com.xpf.p2p.ui.multilanguage.model;

import com.xpf.common.cons.SpKey;
import com.xpf.common.utils.LocaleUtils;
import com.xpf.common.utils.SpUtil;
import com.xpf.p2p.P2PApplication;
import com.xpf.p2p.ui.multilanguage.contract.MultiLanguageContract;
import com.xpf.p2p.ui.multilanguage.listener.OnLanguageListener;
import com.xpf.p2p.ui.multilanguage.listener.OnLanguageSaveListener;

import java.util.Locale;

/**
 * Created by xpf on 2018/6/9 :)
 * GitHub:xinpengfei520
 * Function:
 */
public class MultiLanguageModel implements MultiLanguageContract.IModel {

    @Override
    public void getLanguage(OnLanguageListener listener) {
        int language = SpUtil.getInstance(P2PApplication.getContext())
                .getInt(SpKey.CURRENT_LANGUAGE, 0);
        if (listener != null) {
            listener.onSelected(language);
        }
    }

    @Override
    public void saveSetting(int language, OnLanguageSaveListener listener) {
        SpUtil.getInstance(P2PApplication.getContext())
                .save(SpKey.CURRENT_LANGUAGE, language);
        Locale myLocale = Locale.SIMPLIFIED_CHINESE;
        // 0 简体中文 1 繁体中文 2 English
        switch (language) {
            case 0:
                myLocale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 1:
                myLocale = Locale.TRADITIONAL_CHINESE;
                break;
            case 2:
                myLocale = Locale.ENGLISH;
                break;
        }
        // 本地语言设置
        if (LocaleUtils.needUpdateLocale(P2PApplication.getContext(), myLocale)) {
            LocaleUtils.updateLocale(P2PApplication.getContext(), myLocale);
        }

        if (listener != null) {
            listener.onSuccess();
        }
    }
}
