package com.xpf.p2p.ui.multilanguage.view;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xpf.p2p.R;
import com.xpf.p2p.activity.WelcomeActivity;
import com.xpf.p2p.base.MvpBaseActivity;
import com.xpf.p2p.ui.multilanguage.contract.MultiLanguageContract;
import com.xpf.p2p.ui.multilanguage.presenter.MultiLanguagePresenter;
import com.xpf.p2p.utils.ToastUtil;


public class MultiLanguageActivity extends MvpBaseActivity<MultiLanguageContract.IView,
        MultiLanguagePresenter<MultiLanguageContract.IView>> implements MultiLanguageContract.IView {

    ImageView ivBack;
    TextView tvSave;
    RadioButton rbSimpleChinese;
    RadioButton rbFantiChinese;
    RadioButton rbEnglish;
    RadioGroup rgLanguages;
    private int mCurrentLanguage = 0;

    @Override
    protected MultiLanguagePresenter<MultiLanguageContract.IView> createPresenter() {
        return new MultiLanguagePresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_multi_language;
    }

    @Override
    protected void initData() {
        ivBack = findViewById(R.id.ivBack);
        tvSave = findViewById(R.id.tvSave);
        rbSimpleChinese = findViewById(R.id.rbSimpleChinese);
        rbFantiChinese = findViewById(R.id.rbFantiChinese);
        rbEnglish = findViewById(R.id.rbEnglish);
        rgLanguages = findViewById(R.id.rgLanguages);
        initListener();
        mPresenter.getLanguageSetting();
    }

    private void initListener() {
        ivBack.setOnClickListener(v -> finish());
        tvSave.setOnClickListener(v -> {
            mPresenter.saveSetting(mCurrentLanguage);
        });
        rgLanguages.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSimpleChinese) {
                mCurrentLanguage = 0;
            } else if (checkedId == R.id.rbFantiChinese) {
                mCurrentLanguage = 1;
            } else if (checkedId == R.id.rbEnglish) {
                mCurrentLanguage = 2;
            }
        });
    }

    @Override
    public void showLanguageSetting(int currentLanguage) {
        switch (currentLanguage) {
            case 0:
                rgLanguages.check(R.id.rbSimpleChinese);
                break;
            case 1:
                rgLanguages.check(R.id.rbFantiChinese);
                break;
            case 2:
                rgLanguages.check(R.id.rbEnglish);
                break;
            default:
                break;
        }
    }

    @Override
    public void showSaveSuccess() {
        ToastUtil.show(this, getString(R.string.set_success));
        Intent intent = new Intent(MultiLanguageActivity.this, WelcomeActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
