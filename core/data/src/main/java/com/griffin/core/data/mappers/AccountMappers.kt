package com.griffin.core.data.mappers

import com.griffin.core.data.dto.UserAccountDto
import com.griffin.core.data.model.AccountListModel
import com.griffin.core.data.model.AccountModel

/**
 * 账户信息网络数据转模型
 */
fun List<UserAccountDto>?.toAccountModel(): List<AccountModel> {
    return if (isNullOrEmpty()) {
        return emptyList()
    } else {
        val newList = mutableListOf<AccountModel>()
        val child1 = filter { it.type == "00" }
        val child2 = filter { it.type == "01" }
        newList.add(
            AccountModel(
                title = "电子账户",
                type = "00",
                count = child1.size,
                itemChildList = child1.map {
                    AccountListModel(
                        balance = it.balance,
                        cardCode = it.cardCode,
                        cardName = it.cardName,
                        id = it.id,
                        remark = it.remark,
                        type = it.type

                    )
                }
            )
        )
        newList.add(
            AccountModel(
                title = "储蓄账户",
                type = "01",
                count = child2.size,
                itemChildList = child2.map {
                    AccountListModel(
                        balance = it.balance,
                        cardCode = it.cardCode,
                        cardName = it.cardName,
                        id = it.id,
                        remark = it.remark,
                        type = it.type
                    )
                }
            )
        )
        newList
    }
}

fun AccountListModel.toAccountDto(): UserAccountDto{
    return UserAccountDto(
        balance = balance,
        cardCode = cardCode,
        cardName = cardName,
        id = id,
        remark = remark,
        type = type
    )
}