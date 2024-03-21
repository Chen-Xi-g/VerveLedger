package com.griffin.core.data.models.error

/**
 * 网络请求异常
 *
 * @property code 状态码
 * @property message 错误信息
 * @property exception 异常
 */
data class HttpException(
    val message: String? = null,
    val code: Int = 0,
    val exception: Throwable? = null
)