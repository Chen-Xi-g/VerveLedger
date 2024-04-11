package com.griffin.core.network.source.bill

import com.griffin.core.data.dto.BaseDto
import com.griffin.core.data.dto.BillDetailDto
import com.griffin.core.data.model.BillListModel
import com.griffin.core.data.model.BillModel
import com.griffin.core.data.model.Resource

/**
 * 账单数据仓库
 */
interface BillDataSource {

    /**
     * 获取账单列表
     *
     * @param accountType 账户类型 null-全部，00-电子账户，01-储蓄账户
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param billName 账单名称
     * @param endTime 结束时间 yyyy-MM-dd
     * @param typeTag 消费类型标签 null-全部 0-支出 1-收入
     */
    suspend fun bill(
        accountType: String? = null,
        beginTime: String? = null,
        billName: String? = null,
        endTime: String? = null,
        typeTag: String? = null
    ): Resource<List<BillModel>>

    /**
     * 新增账单
     *
     * @param accountId 账户ID
     * @param address 地址
     * @param billAmount 账单金额
     * @param billId 账单ID
     * @param billName 账单名称
     * @param createTime 创建时间
     * @param latitude 纬度
     * @param longitude 经度
     * @param remark 备注
     * @param typeId 消费类型ID
     */
    suspend fun addBill(
        accountId: Long? = null,
        address: String? = null,
        billAmount: Long = 0,
        billId: Long? = null,
        billName: String = "",
        createTime: String = "",
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        remark: String = "",
        typeId: Long = 0
    ): Resource<String>

    /**
     * 账单详情
     *
     * @param id 账单ID
     */
    suspend fun billDetail(id: Long): Resource<BillListModel>

}