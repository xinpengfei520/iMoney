package com.xpf.p2p.multilanguage.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xpf.common.base.MvpBaseActivity;
import com.xpf.common.utils.ToastUtil;
import com.xpf.p2p.R;
import com.xpf.p2p.activity.WelcomeActivity;
import com.xpf.p2p.ui.multilanguage.contract.MultiLanguageContract;
import com.xpf.p2p.ui.multilanguage.presenter.MultiLanguagePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MultiLanguageActivity extends MvpBaseActivity<MultiLanguageContract.IView,
        MultiLanguagePresenter<MultiLanguageContract.IView>> implements MultiLanguageContract.IView {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.rbSimpleChinese)
    RadioButton rbSimpleChinese;
    @BindView(R.id.rbFantiChinese)
    RadioButton rbFantiChinese;
    @BindView(R.id.rbEnglish)
    RadioButton rbEnglish;
    @BindView(R.id.rgLanguages)
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
        initListener();
        mPresenter.getLanguageSetting();
    }

    private void initListener() {
        rgLanguages.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbSimpleChinese:
                    mCurrentLanguage = 0;
                    break;
                case R.id.rbFantiChinese:
                    mCurrentLanguage = 1;
                    break;
                case R.id.rbEnglish:
                    mCurrentLanguage = 2;
                    break;
            }
        });
    }

    @OnClick({R.id.ivBack, R.id.tvSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvSave:
                mPresenter.saveSetting(mCurrentLanguage);
                break;
        }
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
