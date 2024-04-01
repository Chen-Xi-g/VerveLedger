package com.griffin.core.data.model.state

/**
 * 登录页面的UI状态
 *
 * @property username 用户名
 * @property email 邮箱
 * @property password 密码
 * @property confirmPassword 确认密码
 * @property code 验证码
 * @property uuid 验证码唯一标识
 */
data class LoginState(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var code: String = "",
    var uuid: String = ""
)