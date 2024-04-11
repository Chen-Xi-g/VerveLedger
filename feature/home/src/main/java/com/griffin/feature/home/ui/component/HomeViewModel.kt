package com.griffin.feature.home.ui.component

import androidx.lifecycle.viewModelScope
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.BillModel
import com.griffin.core.network.source.bill.BillDataSource
import com.griffin.core.router.RoutePath
import com.griffin.core.utils.router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val billDataSource: BillDataSource
) : BaseViewModel() {

    /**
     * 账单列表
     */
    val billList = MutableSharedFlow<List<BillModel>>()

    /**
     * 本月收入
     */
    val income = MutableStateFlow(0L)

    /**
     * 本月支出
     */
    val expenses = MutableStateFlow(0L)

    /**
     * 剩余额度
     */
    val surplus = MutableStateFlow(100000L)

    /**
     * 获取账单列表
     *
     * @param accountType 账户类型 null-全部，00-电子账户，01-储蓄账户
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param billName 账单名称
     * @param endTime 结束时间 yyyy-MM-dd
     * @param typeTag 消费类型标签 null-全部 0-支出 1-收入
     */
    fun getBillList(
        accountType: String? = null,
        beginTime: String? = null,
        billName: String? = null,
        endTime: String? = null,
        typeTag: String? = null
    ) {
        handleRequest(
            block = {
                billDataSource.bill()
            }
        ){
            viewModelScope.launch {
                billList.emit(it.data ?: emptyList())
            }
            income.value = it.data?.sumOf { it.income } ?: 0
            expenses.value = it.data?.sumOf { it.expenses } ?: 0
        }
    }

    fun onClickMap(){
        RoutePath.Map.MAP_DETAIL.router {
            putInt("type", 2)
        }
    }

}