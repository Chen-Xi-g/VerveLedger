package com.griffin.core.network.interceptor

import com.griffin.core.router.RoutePath
import com.griffin.core.utils.mmkv.BaseMV
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
        if (proceed.body != null) {
            // 拦截响应体，获取code和Message判断Token是否过期
            val json = proceed.body?.string() ?: ""
            if (json.contains("需要登录")) {
                BaseMV.User.clearAll()
                TheRouter.build(RoutePath.Login.LOGIN)
                    .navigation()
            }
            proceed = proceed.newBuilder()
                .body(json.toResponseBody())
                .build()
        }
        return proceed
    }
}