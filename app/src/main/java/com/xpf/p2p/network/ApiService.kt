package com.xpf.p2p.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Retrofit API 接口定义
 */
interface ApiService {

    @GET
    fun get(@Url url: String): Call<ResponseBody>

    @GET
    fun get(@Url url: String, @QueryMap params: Map<String, String>): Call<ResponseBody>

    @POST
    fun post(@Url url: String): Call<ResponseBody>
}
