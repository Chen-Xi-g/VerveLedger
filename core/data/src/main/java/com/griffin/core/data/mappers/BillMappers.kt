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
            map.toBillListModelDetail()
        }
    }
}

/**
 * 账单详情转换为账单列表模型
 */
fun BillListDto?.toBillListModelDetail(): BillListModel {
    return if (this == null){
        BillListModel()
    }else{
        BillListModel(
            billId = billId,
            userPayTypeDto = userPayType,
            userAccountDto = userAccount,
            billName = billName,
            billAmount = billAmount,
            isIncome = userPayType.isIncome(),
            remark = remark,
            createTime = createTime,
            address = address,
            longitude = longitude,
            latitude = latitude
        )
    }
}