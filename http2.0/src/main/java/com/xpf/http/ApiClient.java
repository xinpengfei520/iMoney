package com.xpf.http;

import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by xpf on 2017/9/22 :)
 * Function: network request config initialize
 */

public class ApiClient {

    private static final ApiClient instance = new ApiClient();

    private ApiClient() {
    }

    public static ApiClient getInstance() {
        return instance;
    }

    /**
     * initialize network request config.
     *
     * @param context
     */
    public void init(Context context) {
        initOkhttpClient(context);
    }

    /**
     * initialize okhttp client config
     *
     * @param context
     */
    private void initOkhttpClient(Context context) {
        if (context == null) return;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .dns(OkHttpDns.getInstance(context))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return hostname.contains("anloq.com");// 替换为自己api的域名
                    }
                })
                .sslSocketFactory(getSSLSocketFactory())
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * https SSL identify verify
     *
     * @return
     */
    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return sslSocketFactory;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }
    }

}
