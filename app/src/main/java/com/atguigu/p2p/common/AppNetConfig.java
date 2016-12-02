package com.atguigu.p2p.common;

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:18091383534
 * Function:提供当前应用访问服务器的请求地址
 */

public class AppNetConfig {

    public static final String HOST = "192.168.31.193"; // 提供ip地址

    // 提供web应用的地址
    public static final String BASE_URL = "http://" + HOST + ":8080/P2PInvest/";

    public static final String INDEX = BASE_URL + "index"; // 访问首页数据

    public static final String LOGIN = BASE_URL + "login"; // 访问登录的url

    public static final String PRODUCT = BASE_URL + "product"; // 访问“所有理财”的url

    public static final String UPDATE = BASE_URL + "update.json"; // 访问服务器端当前应用的版本信息
}
