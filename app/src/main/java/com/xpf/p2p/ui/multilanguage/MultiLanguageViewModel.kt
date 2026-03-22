package com.xpf.p2p.ui.multilanguage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xpf.p2p.App
import com.xpf.p2p.constants.SpKey
import com.xpf.p2p.utils.LocaleUtils
import com.xpf.p2p.utils.SpUtil
import java.util.Locale

/**
 * 多语言设置 ViewModel — 替代原 MultiLanguagePresenter + MultiLanguageModel
 */
class MultiLanguageViewModel : ViewModel() {

    private val _currentLanguage = MutableLiveData<Int>()
    val currentLanguage: LiveData<Int> = _currentLanguage

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    fun getLanguageSetting() {
        val language = SpUtil.getInstance(App.context)
            .getInt(SpKey.CURRENT_LANGUAGE, 0)
        _currentLanguage.value = language
    }

    fun saveSetting(language: Int) {
        SpUtil.getInstance(App.context)
            .save(SpKey.CURRENT_LANGUAGE, language)

        val myLocale = when (language) {
            0 -> Locale.SIMPLIFIED_CHINESE
            1 -> Locale.TRADITIONAL_CHINESE
            2 -> Locale.ENGLISH
            else -> Locale.SIMPLIFIED_CHINESE
        }

        if (LocaleUtils.needUpdateLocale(App.context, myLocale)) {
            LocaleUtils.updateLocale(App.context, myLocale)
        }

        _saveSuccess.value = true
    }
}
