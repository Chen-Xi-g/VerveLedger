package com.griffin.core.network.api

import com.griffin.core.data.dto.BaseDto
import com.griffin.core.data.dto.LoginRequestDto
import com.griffin.core.data.dto.TokenDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    /**
     * 用户登录
     *
     * @param request 登录请求的传输对象
     *
     * @return 返回Token信息
     */
    @POST("login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): BaseDto<TokenDto>

    /**
     * 用户注册
     *
     * @param request 注册请求的传输对象
     *
     * @return 返回注册结果
     */
    @POST("register")
    suspend fun register(
        @Body request: LoginRequestDto
    ): BaseDto<String>

    /**
     * 忘记密码
     *
     * @param request 忘记密码请求的传输对象
     *
     * @return 返回忘记密码结果
     */
    @POST("forgotPwd")
    suspend fun forgetPassword(
        @Body request: LoginRequestDto
    ): BaseDto<String>

    /**
     * 激活账户
     *
     * @param request 激活账户请求的传输对象
     */
    @POST("sendEmailActivate")
    suspend fun activate(
        @Body request: LoginRequestDto
    ): BaseDto<String>

}