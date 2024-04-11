package com.griffin.core.network.constant

import com.griffin.core.data.model.Resource
import retrofit2.HttpException

object NetworkConstant {

    /**
     * 默认URL前缀 /api/app
     */
    const val BASE_URL = "/api/app/"

}

inline fun <reified T> Exception.toNetError(data: T? = null): Resource.Error<T> {
    printStackTrace()
    val code = when(this){
        is HttpException -> code()
        else -> 500
    }
    val message = when (code) {
        400 -> "错误的请求，请稍后重试"
        401 -> "未授权，请登录后重试"
        403 -> "您无权访问该资源"
        404 -> "请求的页面不存在，请稍后重试或返回上一页"
        408 -> "请求超时，请稍后重试"
        500 -> "服务器内部错误，请稍后重试"
        502 -> "应用正在更新，请稍后重试"
        503 -> "服务暂时不可用，请稍后重试"
        else -> "未知错误，请稍后重试"
    }
    return Resource.Error(message = message,data = `data`, code = code)
}