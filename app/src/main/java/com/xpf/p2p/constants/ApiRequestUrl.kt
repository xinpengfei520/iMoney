package com.xpf.p2p.constants

import com.xpf.p2p.BuildConfig

/**
 * Created by xpf on 2016/11/12 :)
 * Function:提供当前应用访问服务器的请求地址
 */
object ApiRequestUrl {
    const val HOST: String = BuildConfig.HOST
    const val BASE_URL: String = "$HOST/iMoney/"
    const val INDEX: String = "${BASE_URL}index"
    const val LOGIN: String = "${BASE_URL}login"
    const val PRODUCT: String = "${BASE_URL}product"
    const val UPDATE: String = "${BASE_URL}update"
    const val STUDENTS: String = "${BASE_URL}students"
}
