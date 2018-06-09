package com.xpf.http.httpdns;

import android.content.Context;

import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;
import com.xpf.http.logger.XLog;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

/**
 * Created by xpf on 2017/6/6 :)
 * Function:OkHttpDns domain name parser
 */

public class OkHttpDns implements Dns {

    private static final Dns SYSTEM = Dns.SYSTEM;
    HttpDnsService httpDns; // httpDns 解析服务

    private static OkHttpDns instance = null;

    private OkHttpDns(Context context) {
        this.httpDns = HttpDns.getService(context, "164485");// 替换为自己申请的
    }

    public static OkHttpDns getInstance(Context context) {
        if (instance == null) {
            instance = new OkHttpDns(context);
        }
        return instance;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        String ip = httpDns.getIpByHost(hostname);
        if (ip != null) {
            List<InetAddress> iNetAddresses = Arrays.asList(InetAddress.getAllByName(ip));
            XLog.i("iNetAddresses:" + iNetAddresses);
            return iNetAddresses;
        }
        return SYSTEM.lookup(hostname);
    }

}
