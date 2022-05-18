package com.xpf.common.cons;

import com.xpf.common.BuildConfig;

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:18091383534
 * Function:提供当前应用访问服务器的请求地址
 */
public class ApiRequestUrl {

    public static final String HOST = BuildConfig.HOST; // 本地服务器地址

    public static final String BASE_URL = HOST + "/iMoney/";

    public static final String INDEX = BASE_URL + "index"; // 访问首页数据

    public static final String LOGIN = BASE_URL + "login"; // 访问登录的url

    public static final String PRODUCT = BASE_URL + "product"; // 访问“所有理财”的url

    public static final String UPDATE = BASE_URL + "update"; // 访问服务器端当前应用的版本信息

    public static final String STUDENTS = BASE_URL + "students";
}
