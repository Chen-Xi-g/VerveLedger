package com.griffin.feature.add.component

import androidx.lifecycle.viewModelScope
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.dto.UserAccountDto
import com.griffin.core.data.dto.UserPayTypeDto
import com.griffin.core.data.model.BillListModel
import com.griffin.core.data.model.MapLocationModel
import com.griffin.core.data.model.TypeChildModel
import com.griffin.core.domain.use_case.validation.bill.ValidationBill
import com.griffin.core.network.source.bill.BillDataSource
import com.griffin.core.utils.y2f
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val billDataSource: BillDataSource,
    private val validationBill: ValidationBill
) : BaseViewModel() {

    companion object {
        /**
         * 账单类型
         */
        const val START_BILL_TYPE = 1

        /**
         * 账单账户
         */
        const val START_BILL_ACCOUNT = 2

        /**
         * 账单地址
         */
        const val START_BILL_ADDRESS = 3

        /**
         * 关闭页面
         */
        const val FINISH_OK = 4
    }

    /**
     * 页面状态
     */
    val uiState = MutableStateFlow(BillListModel())

    /**
     * 金额
     */
    val amount = MutableStateFlow("")

    /**
     * 跳转到指定Activity
     */
    private val _navigateTo = MutableSharedFlow<Int>()
    val navigateTo = _navigateTo.asSharedFlow()

    /**
     * 提交表单
     */
    fun submit() {
        uiState.value.billAmount = amount.value.y2f()
        val errorMessage = validationBill.execute(
            billId = uiState.value.billId,
            billName = uiState.value.billName,
            userPayTypeDto = uiState.value.userPayTypeDto,
            billAmount = amount.value.y2f()
        )
        if (!errorMessage.successful) {
            error(
                message = errorMessage.errorMessage ?: "未知错误",
                isToast = true
            )
            return
        }
        handleRequest(
            block = {
                billDataSource.addBill(
                    accountId = uiState.value.userAccountDto.id,
                    address = uiState.value.address,
                    billAmount = amount.value.y2f(),
                    billName = uiState.value.billName,
                    createTime = uiState.value.createTime,
                    latitude = uiState.value.latitude,
                    longitude = uiState.value.longitude,
                    remark = uiState.value.remark,
                    typeId = uiState.value.userPayTypeDto.typeId
                )
            },
            isLoadingDialog = true,
            message = "正在提交...",
            errorType = ERROR_DIALOG
        ) {
            viewModelScope.launch {
                _navigateTo.emit(FINISH_OK)
            }
        }
    }

    /**
     * 跳转到指定Activity
     */
    fun onClick(startType: Int) {
        viewModelScope.launch {
            _navigateTo.emit(startType)
        }
    }

    /**
     * 更新账单类型数据
     *
     * @param type 消费类型
     */
    fun updatePayType(type: TypeChildModel) {
        uiState.value = uiState.value.copy(
            userPayTypeDto = UserPayTypeDto(
                parentId = type.parentId,
                typeId = type.typeId,
                typeName = type.typeName,
                typeTag = type.typeTag
            ),
            isIncome = type.typeTag == "1"
        )
    }

    /**
     * 更新账户
     *
     * @param account 账户信息
     */
    fun updateAccount(account: UserAccountDto) {
        uiState.value = uiState.value.copy(
            userAccountDto = account
        )
    }

    /**
     * 更新地址
     *
     * @param location 地址实体类
     */
    fun updateLocation(location: MapLocationModel) {
        uiState.value = uiState.value.copy(
            address = location.address,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

}