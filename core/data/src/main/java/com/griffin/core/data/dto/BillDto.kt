package com.griffin.core.data.dto
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

/**
 * 账单明细
 *
 * @param children 子账单
 * @param createTime 创建时间
 * @param expenditure 支出
 * @param income 收入
 */
@Serializable
data class BillDetailDto(
    @SerialName("children")
    val children: List<BillListDto> = listOf(),
    @SerialName("createTime")
    val createTime: String = "",
    @SerialName("expenditure")
    val expenditure: Int = 0,
    @SerialName("income")
    val income: Int = 0
)

/**
 * 子账单
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
 * @param userAccount 用户账户
 * @param userPayType 用户支付类型
 */
@Serializable
data class BillListDto(
    @SerialName("accountId")
    val accountId: Long = 0,
    @SerialName("address")
    val address: String = "",
    @SerialName("billAmount")
    val billAmount: Int = 0,
    @SerialName("billId")
    val billId: Long = 0,
    @SerialName("billName")
    val billName: String = "",
    @SerialName("createTime")
    val createTime: String = "",
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0,
    @SerialName("remark")
    val remark: String = "",
    @SerialName("typeId")
    val typeId: Long = 0,
    @SerialName("userAccount")
    val userAccount: UserAccountDto = UserAccountDto(),
    @SerialName("userPayType")
    val userPayType: UserPayTypeDto = UserPayTypeDto()
)

/**
 * 用户账户
 *
 * @param balance 余额
 * @param cardCode 卡号
 * @param cardName 卡名称
 * @param id ID
 * @param remark 备注
 * @param type 类型
 */
@Serializable
data class UserAccountDto(
    @SerialName("balance")
    val balance: Int = 0,
    @SerialName("cardCode")
    val cardCode: String = "",
    @SerialName("cardName")
    val cardName: String = "",
    @SerialName("id")
    val id: Long = 0,
    @SerialName("remark")
    val remark: String = "",
    @SerialName("type")
    val type: String = ""
): java.io.Serializable

/**
 * 用户支付类型
 *
 * @param child 子数据
 * @param parentId 父级ID
 * @param typeId 类型ID
 * @param typeName 类型名称
 * @param typeTag 类型标签 0支出 1收入
 */
@Serializable
data class UserPayTypeDto(
    @SerialName("child")
    val child: List<UserPayTypeDto> = listOf(),
    @SerialName("parentId")
    val parentId: Long = 0,
    @SerialName("typeId")
    val typeId: Long = 0,
    @SerialName("typeName")
    val typeName: String = "",
    @SerialName("typeTag")
    val typeTag: String = ""
)

/**
 * 是否是收入
 */
fun UserPayTypeDto.isIncome() = this.typeTag == "1"

/**
 * 新增账单的请求体
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
@Serializable
data class AddBillDto(
    val accountId: Long? = null,
    val address: String? = null,
    val billAmount: Long,
    val billId: Long? = null,
    val billName: String,
    val createTime: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val remark: String,
    val typeId: Long
)
