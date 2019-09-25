package com.xpf.p2p.ui.multilanguage.presenter;

import com.xpf.common.base.MvpBasePresenter;
import com.xpf.p2p.ui.multilanguage.contract.MultiLanguageContract;
import com.xpf.p2p.ui.multilanguage.model.MultiLanguageModel;

/**
 * Created by xpf on 2018/6/9 :)
 * GitHub:xinpengfei520
 * Function:
 */
public class MultiLanguagePresenter<T extends MultiLanguageContract.IView>
        extends MvpBasePresenter<T> implements MultiLanguageContract.IPresenter {

    private MultiLanguageModel mModel = new MultiLanguageModel();

    @Override
    public void getLanguageSetting() {
        mModel.getLanguage(language -> {
            if (isNonNull()) {
                mViewRef.get().showLanguageSetting(language);
            }
        });
    }

    @Override
    public void saveSetting(int language) {
        mModel.saveSetting(language, () -> {
            if (isNonNull()) {
                mViewRef.get().showSaveSuccess();
            }
        });
    }
}
