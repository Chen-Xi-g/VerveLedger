package com.griffin.core.network.interceptor

import com.google.gson.Gson
import com.griffin.core.router.RoutePath
import com.griffin.core.utils.mmkv.BaseMV
import com.griffin.core.utils.router
import com.therouter.TheRouter
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * 返回参数拦截器
 */
class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 获取返回体，从中获取Cookie持久化登录。
        var proceed = chain.proceed(chain.request())
        if (proceed.body != null && proceed.isSuccessful) {
            // 拦截响应体，获取code和Message判断Token是否过期
            val json = proceed.body?.string() ?: ""
            val fromJson = Gson().fromJson(json, HashMap::class.java)
            if (proceed.code == 401 || (fromJson.get("code").toString().toDoubleOrNull() ?: 0.0) == 401.0) {
                BaseMV.User.clearAll()
                RoutePath.Login.LOGIN.router()
            }
            proceed = proceed.newBuilder()
                .body(json.toResponseBody())
                .build()
        }
        return proceed
    }
}