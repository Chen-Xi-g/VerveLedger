package com.griffin.core.data.dto

import kotlinx.serialization.Serializable

/**
 * Token的传输对象
 *
 * @property token 用户登录的token
 */
@Serializable
data class TokenDto(
    val token: String = ""
)

/**
 * 登录请求的传输对象
 *
 * @property username 用户名
 * @property email 邮箱
 * @property password 密码
 * @property confirmPassword 确认密码
 * @property code 验证码
 * @property uuid 验证码唯一标识
 */
@Serializable
data class LoginRequestDto(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val code: String? = null,
    val uuid: String? = null
)