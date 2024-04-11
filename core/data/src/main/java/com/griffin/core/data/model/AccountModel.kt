package com.griffin.core.data.model

import com.griffin.core.rv.model.ItemExpand

/**
 * 账户信息
 *
 * @property title 标题
 * @property type 类型
 * @property count 数量
 */
data class AccountModel(
    val title: String = "",
    val type: String = "",
    val count: Int = 0,
    override var itemGroupPosition: Int = 0,
    override var itemExpand: Boolean = true,
    override var itemChildList: List<Any?>? = emptyList<AccountListModel>()
): ItemExpand

/**
 * 账户信息
 *
 * @property balance 余额
 * @property cardCode 卡号
 * @property cardName 卡名
 * @property id ID
 * @property remark 备注
 * @property type 类型（00：电子账户，01：储蓄账户）
 */
data class AccountListModel(
    val balance: Long = 0,
    val cardCode: String = "",
    val cardName: String = "",
    val id: Long = 0,
    val remark: String = "",
    val type: String = ""
)