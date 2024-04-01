package com.griffin.core.domain.use_case.validation.bill

import com.griffin.core.data.dto.UserPayTypeDto
import com.griffin.core.data.model.BillListModel
import com.griffin.core.domain.use_case.validation.user.ValidationResult

class ValidationBill {

    /**
     * 校验账单数据是否完整
     *
     * @param billId 账单ID
     * @param billName 账单名称
     * @param userPayTypeDto 用户支付类型
     * @param billAmount 账单金额
     * @param isAdd 是否是新增账单
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(billId: Long, billName: String, userPayTypeDto: UserPayTypeDto, billAmount: Long, isAdd: Boolean = true): ValidationResult {
        val errorMsg = if(billName.isBlank()){
            "请输入账单名称"
        } else if (billName.length > 15){
            "账单名称不能超过15个字符"
        }else if (userPayTypeDto.typeId == 0L || userPayTypeDto.typeName.isBlank()){
            "请选择分类"
        }else if (billAmount == 0L){
            "金额不能为0"
        }else{
            ""
        }
        if (errorMsg.isNotBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = errorMsg
            )
        }
        if (!isAdd){
            if (billId == 0L){
                return ValidationResult(
                    successful = false,
                    errorMessage = "账单ID不能为空"
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }

}