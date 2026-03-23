package com.xpf.p2p.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * OkHttp + Retrofit 网络客户端单例
 */
object RetrofitClient {

    private const val CONNECT_TIMEOUT = 10L
    private const val READ_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 10L

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost/")
            .client(okHttpClient)
            .build()
        retrofit.create(ApiService::class.java)
    }
}
