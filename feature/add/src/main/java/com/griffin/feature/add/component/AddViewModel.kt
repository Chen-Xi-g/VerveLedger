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
import com.griffin.core.utils.TimeUtils
import com.griffin.core.utils.TimeUtils.toFormatTime
import com.griffin.core.utils.f2y
import com.griffin.core.utils.y2f
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
         * 日期
         */
        const val START_BILL_DATE = 2

        /**
         * 账单账户
         */
        const val START_BILL_ACCOUNT = 3

        /**
         * 账单地址
         */
        const val START_BILL_ADDRESS = 4

        /**
         * 修改账单
         */
        const val START_BILL_UPDATE = 5

        /**
         * 关闭页面
         */
        const val FINISH_OK = 6
    }

    /**
     * 页面状态
     */
    val uiState = MutableStateFlow(BillListModel(createTime = TimeUtils.currentStr(TimeUtils.FORMAT_YYYY_MM_DD_HH_MM)))

    /**
     * 金额
     */
    val amount = MutableStateFlow("")

    /**
     * 页面类型
     *
     * 0：新增 1：修改 2：查看
     */
    var type: Int = 0

    /**
     * 跳转到指定Activity
     */
    private val _navigateTo = MutableSharedFlow<Int>()
    val navigateTo = _navigateTo.asSharedFlow()

    /**
     * 获取账单详情
     *
     * @param billId 账单ID
     */
    fun getBillDetail(billId: Long?) {
        if (billId == null) return
        handleRequest(
            block = {
                billDataSource.billDetail(billId)
            },
            isLoadingDialog = true,
            message = "正在加载...",
            errorType = ERROR_DIALOG
        ) {
            it.data?.let { bill ->
                bill.createTime = bill.createTime.toFormatTime(format = TimeUtils.FORMAT_YYYY_MM_DD_HH_MM)
                uiState.value = bill
                amount.value = bill.billAmount.f2y()
            }
        }
    }

    /**
     * 提交表单
     */
    fun submit() {
        when(type){
            AddActivity.PARAMS_TYPE_ADD -> {
                addOrUpdateBill()
            }
            AddActivity.PARAMS_TYPE_DETAIL -> {
                onClick(START_BILL_UPDATE)
            }
            AddActivity.PARAMS_TYPE_UPDATE -> {
                addOrUpdateBill()
            }
        }
    }

    private fun addOrUpdateBill(){
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
                    createTime = uiState.value.createTime + ":00",
                    latitude = uiState.value.latitude,
                    longitude = uiState.value.longitude,
                    remark = uiState.value.remark,
                    typeId = uiState.value.userPayTypeDto.typeId,
                    billId = if (uiState.value.billId == 0L) null else uiState.value.billId
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

    /**
     * 更新时间
     *
     * @param date 时间
     */
    fun updateDate(date: String){
        uiState.value = uiState.value.copy(
            createTime = date
        )
    }

}