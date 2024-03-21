package com.griffin.core.data.dto

import kotlinx.serialization.Serializable

/**
 * 传输对象基类
 *
 * @param T 传输对象
 * @param code 状态码
 * @param msg 状态信息
 * @param data 数据
 */
@Serializable
data class BaseDto<T>(
    val code: Int = 0,
    val msg: String? = null,
    val `data`: T? = null
){

    /**
     * 是否成功
     *
     * @return true 成功, false 失败
     */
    fun isSuccess() = code == 200
}