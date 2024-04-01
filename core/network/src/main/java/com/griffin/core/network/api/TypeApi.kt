package com.griffin.core.network.api

import com.griffin.core.data.dto.BaseDto
import com.griffin.core.data.dto.UserPayTypeDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 类型API
 */
interface TypeApi {

    /**
     * 获取消费类型列表
     *
     * @param typeTag 类型标签（0：支出，1：收入）
     */
    @GET("getPayTypeAndChild")
    suspend fun getPayTypeAndChild(@Query("typeTag") typeTag: String): BaseDto<List<UserPayTypeDto>>

}