package com.xpf.p2p.utils

import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * 校验器：利用正则表达式校验邮箱、手机号等
 */
object Validator {

    const val REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$"
    const val REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$"
    const val REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
    const val REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
    const val REGEX_CHINESE = "^[\u4e00-\u9fa5]{1,9}$"
    const val REGEX_ID_CARD = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])"
    const val REGEX_IC = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)"
    const val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"
    const val REGEX_IP_ADDR = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})"

    @JvmStatic
    @Throws(PatternSyntaxException::class)
    fun isChinaPhoneLegal(str: String): Boolean {
        val regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(str)
        return m.matches()
    }

    @JvmStatic
    fun isUserName(username: String): Boolean = Pattern.matches(REGEX_USERNAME, username)

    @JvmStatic
    fun isPassword(password: String): Boolean = Pattern.matches(REGEX_PASSWORD, password)

    @JvmStatic
    fun isMobile(mobile: String): Boolean = Pattern.matches(REGEX_MOBILE, mobile)

    @JvmStatic
    fun isEmail(email: String): Boolean = Pattern.matches(REGEX_EMAIL, email)

    @JvmStatic
    fun isChinese(chinese: String): Boolean = Pattern.matches(REGEX_CHINESE, chinese)

    @JvmStatic
    fun isIDCard(idCard: String): Boolean = Pattern.matches(REGEX_ID_CARD, idCard)

    @JvmStatic
    fun isICard(idCard: String): Boolean = Pattern.matches(REGEX_IC, idCard)

    @JvmStatic
    fun isUrl(url: String): Boolean = Pattern.matches(REGEX_URL, url)

    @JvmStatic
    fun isIPAddress(ipAddress: String): Boolean = Pattern.matches(REGEX_IP_ADDR, ipAddress)
}
