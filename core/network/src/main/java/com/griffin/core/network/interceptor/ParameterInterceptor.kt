package com.griffin.core.network.interceptor

import com.griffin.core.utils.mmkv.BaseMV
import okhttp3.Interceptor
import okhttp3.Response


/**
 * 请求参数拦截器
 */
class ParameterInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestBuilder = request.newBuilder()
        /* 添加请求头参数 */
        val headerBuilder = request.headers.newBuilder()
        headerBuilder.add("Authorization", "Bearer ${BaseMV.User.token ?: ""}")
        requestBuilder.headers(headerBuilder.build())
        request = requestBuilder.build()
        return chain.proceed(request)
    }
}