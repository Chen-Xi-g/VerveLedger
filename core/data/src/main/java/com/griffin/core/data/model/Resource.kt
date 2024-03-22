package com.griffin.core.data.model

import com.griffin.core.data.dto.BaseDto

/**
 * 网络请求资源
 */
sealed class Resource<T>(
    val `data`: T? = null,
    val code: Int = 0,
    val message: String? = null,
) {

    /**
     * 成功
     *
     * @param data 返回的数据
     * @param code 返回的状态码
     * @param message 返回的信息
     */
    class Success<T>(`data`: T?, code: Int = 200, message: String? = null) : Resource<T>(`data`, code, message)

    /**
     * 失败
     *
     * @param message 返回的信息
     * @param code 返回的状态码
     * @param data 返回的数据
     */
    class Error<T>(message: String, code: Int = 0, `data`: T? = null) : Resource<T>(`data`, code, message)
}

/**
 * 数据传输对象转资源
 *
 * @param block 数据转换
 */
fun <T,V> BaseDto<T>.toResource(
    block: ((T?) -> V)? = null
): Resource<V> {
    return if (isSuccess()) {
        Resource.Success(block?.invoke(`data`) ?: `data` as V, code, msg ?: "请求成功")
    } else {
        Resource.Error(msg ?: "请求失败", code)
    }
}