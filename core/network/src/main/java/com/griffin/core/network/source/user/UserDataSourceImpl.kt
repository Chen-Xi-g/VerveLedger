package com.griffin.core.network.source.user

import com.griffin.core.data.dto.LoginRequestDto
import com.griffin.core.data.model.Resource
import com.griffin.core.data.model.toResource
import com.griffin.core.network.api.UserApi
import com.griffin.core.network.constant.toNetError
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userApi: UserApi
) : UserDataSource {
    override suspend fun login(
        code: String,
        password: String,
        username: String,
        uuid: String
    ): Resource<String> {
        return try {
            userApi.login(
                LoginRequestDto(
                    code = code,
                    password = password,
                    username = username,
                    uuid = uuid
                )
            ).toResource {
                it?.token ?: ""
            }
        } catch (e: Exception) {
            e.toNetError()
        }
    }

    override suspend fun register(
        code: String,
        email: String,
        password: String,
        username: String,
        uuid: String
    ): Resource<String> {
        return try {
            userApi.register(
                LoginRequestDto(
                    code = code,
                    email = email,
                    password = password,
                    username = username,
                    uuid = uuid
                )
            ).toResource {
                it ?: "注册成功"
            }
        } catch (e: Exception) {
            e.toNetError()
        }
    }

    override suspend fun forgetPassword(
        code: String,
        confirmPassword: String,
        password: String,
        username: String,
        uuid: String
    ): Resource<String> {
        return try {
            userApi.forgetPassword(
                LoginRequestDto(
                    code = code,
                    confirmPassword = confirmPassword,
                    password = password,
                    username = username,
                    uuid = uuid
                )
            ).toResource {
                it ?: "修改成功"
            }
        }catch (e: Exception){
            e.toNetError()
        }
    }

    override suspend fun activate(code: String, username: String, uuid: String): Resource<String> {
        return try {
            userApi.activate(
                LoginRequestDto(
                    code = code,
                    username = username,
                    uuid = uuid
                )
            ).toResource {
                it ?: "激活成功"
            }
        }catch (e: Exception){
            e.toNetError()
        }
    }
}