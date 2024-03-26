package com.griffin.core.network.source.user

import com.griffin.core.data.model.Resource

/**
 * 用户数据仓库
 */
interface UserDataSource {

    /**
     * 用户登录
     *
     * @param code 验证码内容
     * @param password 密码
     * @param username 用户名
     * @param uuid 验证码uuid
     */
    suspend fun login(code: String, password: String, username: String, uuid: String): Resource<String>

    /**
     * 用户注册
     *
     * @param code 验证码内容
     * @param email 邮箱
     * @param password 密码
     * @param username 用户名
     * @param uuid 验证码uuid
     */
    suspend fun register(code: String, email: String, password: String, username: String, uuid: String): Resource<String>

    /**
     * 忘记密码
     *
     * @param code 验证码内容
     * @param confirmPassword 确认密码
     * @param password 密码
     * @param username 用户名
     * @param uuid 验证码uuid
     */
    suspend fun forgetPassword(code: String, confirmPassword: String, password: String, username: String, uuid: String): Resource<String>

    /**
     * 激活账户
     *
     * @param code 验证码内容
     * @param username 用户名
     * @param uuid 验证码uuid
     */
    suspend fun activate(code: String, username: String, uuid: String): Resource<String>
}