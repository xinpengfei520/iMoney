package com.xpf.common.cons;

/**
 * Created by xpf on 2016/11/12 :)
 * Wechat:18091383534
 * Function:提供当前应用访问服务器的请求地址
 */

public class ApiRequestUrl {

    //    public static final String HOST = "192.168.31.193"; // 提供ip地址
    public static final String HOST = "10.0.2.2"; // 本地服务器地址

    // 提供web应用的地址
    public static final String BASE_URL = "http://" + HOST + ":8080/P2PInvest/";

    private static final String WEB_ROOT = "WebRoot/";
    private static final String FILE = "file/";
    private static final String JSON = ".json";

    public static final String INDEX = BASE_URL + WEB_ROOT + FILE + "index" + JSON; // 访问首页数据

    public static final String LOGIN = BASE_URL + WEB_ROOT + FILE + "login" + JSON; // 访问登录的url

    public static final String PRODUCT = BASE_URL + WEB_ROOT + FILE + "product" + JSON; // 访问“所有理财”的url

    public static final String UPDATE = BASE_URL + WEB_ROOT + "update" + JSON; // 访问服务器端当前应用的版本信息
}
