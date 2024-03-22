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
    return Resource.Error(message = message ?: "未知错误",data = `data`, code = code)
}