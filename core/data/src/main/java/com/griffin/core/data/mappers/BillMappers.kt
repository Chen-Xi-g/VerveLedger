package com.griffin.core.data.mappers

import com.griffin.core.data.dto.BillDetailDto
import com.griffin.core.data.dto.BillListDto
import com.griffin.core.data.dto.isIncome
import com.griffin.core.data.model.BillListModel
import com.griffin.core.data.model.BillModel

/**
 * 账单详情数据传输对象转换为账单模型
 */
fun List<BillDetailDto>?.toBillModel(): List<BillModel>{
    return if (isNullOrEmpty()){
        return emptyList()
    }else{
        mapIndexed { index, billDetailDto ->
            BillModel(
                id = index.toLong(),
                createTime = billDetailDto.createTime,
                income = billDetailDto.income,
                expenses = billDetailDto.expenditure,
                itemExpand = index <= 2,
                itemChildList = billDetailDto.children.toBillListModel()
            )
        }
    }
}

/**
 * 账单列表数据传输对象转换为账单列表模型
 */
fun List<BillListDto>?.toBillListModel(): List<BillListModel>{
    return if (isNullOrEmpty()){
        return emptyList()
    }else{
        map { map ->
            BillListModel(
                billId = map.billId,
                userPayTypeDto = map.userPayType,
                userAccountDto = map.userAccount,
                billName = map.billName,
                billAmount = map.billAmount.toLong(),
                isIncome = map.userPayType.isIncome(),
                remark = map.remark,
                createTime = map.createTime,
                address = map.address,
                longitude = map.longitude,
                latitude = map.latitude
            )
        }
    }
}