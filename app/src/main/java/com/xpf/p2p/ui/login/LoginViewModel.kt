package com.xpf.p2p.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xpf.p2p.App
import com.xpf.p2p.constants.SpKey
import com.xpf.p2p.utils.SpUtil
import com.xpf.p2p.utils.Validator
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK

/**
 * 登录 ViewModel — 替代原 LoginPresenter + LoginModel
 */
class LoginViewModel : ViewModel() {

    companion object {
        private const val COUNTRY_CODE = "86"
    }

    /** 短信发送结果：true=成功, false=失败 */
    private val _smsSendResult = MutableLiveData<Boolean>()
    val smsSendResult: LiveData<Boolean> = _smsSendResult

    /** 验证结果：true=登录成功, false=验证码错误 */
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    /** 错误提示信息 */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /** 是否开始倒计时 */
    private val _startCountDown = MutableLiveData<Boolean>()
    val startCountDown: LiveData<Boolean> = _startCountDown

    fun sendSmsCode(phone: String) {
        if (phone.isEmpty()) {
            _errorMessage.postValue("请输入手机号码！")
            return
        }
        if (phone.length != 11 || !Validator.isChinaPhoneLegal(phone)) {
            _errorMessage.postValue("手机号码非法！")
            return
        }

        _startCountDown.postValue(true)

        SMSSDK.registerEventHandler(object : EventHandler() {
            override fun afterEvent(event: Int, result: Int, data: Any?) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    _smsSendResult.postValue(true)
                } else {
                    _smsSendResult.postValue(false)
                }
            }
        })
        SMSSDK.getVerificationCode(COUNTRY_CODE, phone)
    }

    fun verifySmsCode(phone: String, code: String) {
        if (phone.isEmpty() || code.isEmpty()) {
            _errorMessage.postValue("手机号或验证码不能为空！")
            return
        }
        if (phone.length != 11 || !Validator.isChinaPhoneLegal(phone)) {
            _errorMessage.postValue("手机号码非法！")
            return
        }

        SMSSDK.registerEventHandler(object : EventHandler() {
            override fun afterEvent(event: Int, result: Int, data: Any?) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    saveLoginState()
                    _loginResult.postValue(true)
                } else {
                    _loginResult.postValue(false)
                }
            }
        })
        SMSSDK.submitVerificationCode(COUNTRY_CODE, phone, code)
    }

    private fun saveLoginState() {
        val millis = System.currentTimeMillis()
        SpUtil.getInstance(App.context)
            .save(SpKey.LOGIN_SUCCESS_TIMESTAMP, millis.toString())
    }

    override fun onCleared() {
        super.onCleared()
        SMSSDK.unregisterAllEventHandler()
    }
}
