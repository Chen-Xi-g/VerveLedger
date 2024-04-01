package com.griffin.core.network.api

import com.griffin.core.data.dto.AddBillDto
import com.griffin.core.data.dto.BaseDto
import com.griffin.core.data.dto.BillDetailDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BillApi{

    /**
     * 获取账单列表
     *
     * @param accountType 账户类型 null-全部，00-电子账户，01-储蓄账户
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param billName 账单名称
     * @param endTime 结束时间 yyyy-MM-dd
     * @param typeTag 消费类型标签 null-全部 0-支出 1-收入
     */
    @GET("getBill")
    suspend fun bill(
        @Query("accountType") accountType: String? = null,
        @Query("beginTime") beginTime: String? = null,
        @Query("billName") billName: String? = null,
        @Query("endTime") endTime: String? = null,
        @Query("typeTag") typeTag: String? = null
    ): BaseDto<List<BillDetailDto>>

    /**
     * 新增账单
     *
     * @param bill AddBillDto 新增账单请求体
     */
    @POST("addOrUpdateBill")
    suspend fun addBill(
        @Body bill: AddBillDto
    ): BaseDto<String>

}