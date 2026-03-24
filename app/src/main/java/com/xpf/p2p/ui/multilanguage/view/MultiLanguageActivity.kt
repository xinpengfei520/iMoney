package com.xpf.p2p.ui.multilanguage.view

import android.content.Intent
import android.view.LayoutInflater
import com.xpf.p2p.R
import com.xpf.p2p.activity.WelcomeActivity
import com.xpf.p2p.base.BaseVmActivity
import com.xpf.p2p.databinding.ActivityMultiLanguageBinding
import com.xpf.p2p.ui.multilanguage.MultiLanguageViewModel
import com.xpf.p2p.utils.ToastUtil

/**
 * 多语言设置页面 — MVVM 版本
 */
class MultiLanguageActivity : BaseVmActivity<ActivityMultiLanguageBinding, MultiLanguageViewModel>() {

    private var mCurrentLanguage = 0

    override fun createViewBinding(inflater: LayoutInflater) = ActivityMultiLanguageBinding.inflate(inflater)

    override fun getViewModelClass(): Class<MultiLanguageViewModel> =
        MultiLanguageViewModel::class.java

    override fun initView() {
        binding.ivBack.setOnClickListener { finish() }
        binding.tvSave.setOnClickListener { viewModel.saveSetting(mCurrentLanguage) }
        binding.rgLanguages.setOnCheckedChangeListener { _, checkedId ->
            mCurrentLanguage = when (checkedId) {
                R.id.rbSimpleChinese -> 0
                R.id.rbFantiChinese -> 1
                R.id.rbEnglish -> 2
                else -> 0
            }
        }

        viewModel.getLanguageSetting()
    }

    override fun observeData() {
        viewModel.currentLanguage.observe(this) { language ->
            when (language) {
                0 -> binding.rgLanguages.check(R.id.rbSimpleChinese)
                1 -> binding.rgLanguages.check(R.id.rbFantiChinese)
                2 -> binding.rgLanguages.check(R.id.rbEnglish)
            }
        }

        viewModel.saveSuccess.observe(this) { success ->
            if (success) {
                ToastUtil.show(this, getString(R.string.set_success))
                val intent = Intent(this, WelcomeActivity::class.java).apply {
                    action = Intent.ACTION_MAIN
                    addCategory(Intent.CATEGORY_LAUNCHER)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
    }
}
