package com.xpf.p2p.constants

/**
 * Created by xpf on 2017/12/9 :)
 * Function:提供一个枚举类:将当前联网以后的状态以及可能返回的数据,封装在枚举类中
 */
enum class ResultState(@JvmField val state: Int) {
    ERROR(2),
    EMPTY(3),
    SUCCESS(4);

    var content: String? = null
}
