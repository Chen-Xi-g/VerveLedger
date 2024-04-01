package com.griffin.core.network.api

import com.griffin.core.data.dto.BaseDto
import com.griffin.core.data.dto.UserAccountDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 账户相关接口
 */
interface AccountApi {

    /**
     * 获取账户列表
     *
     * @param type 账户类型(00: 电子账户，01：储蓄账户)不传查全部
     */
    @GET("accountList")
    suspend fun getAccountList(@Query("type") type: Int?): BaseDto<List<UserAccountDto>>

}