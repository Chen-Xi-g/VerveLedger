package com.griffin.feature.map.component.detail

import androidx.lifecycle.viewModelScope
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.BillListModel
import com.griffin.core.network.source.bill.BillDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 地图详情
 */
@HiltViewModel
class MapDetailViewModel @Inject constructor(
    private val billDataSource: BillDataSource
) : BaseViewModel() {

    /**
     * 账单详情
     */
    private val _billDetail = MutableSharedFlow<BillListModel>()
    val billDetail = _billDetail.asSharedFlow()

    /**
     * 账单列表
     */
    private val _billList = MutableSharedFlow<List<BillListModel>>()
    val billList = _billList.asSharedFlow()

    /**
     * 获取账单详情
     *
     * @param id 账单id
     */
    fun loadDetail(id: Long) {
        if (id == 0L) return
        handleRequest(
            block = {
                billDataSource.billDetail(id)
            },
            isLoadingDialog = true
        ) {
            it.data?.let { data ->
                viewModelScope.launch {
                    _billDetail.emit(data)
                }
            }
        }
    }

    /**
     * 获取所有账单
     */
    fun loadList(){
        handleRequest(
            block = {
                billDataSource.bill()
            },
            isLoadingDialog = true
        ){
            viewModelScope.launch {
                val newList = mutableListOf<BillListModel>()
                it.data?.forEach {
                    it.itemChildList?.forEach { child ->
                        if (child is BillListModel && child.address.isNotEmpty() && child.latitude != 0.0 && child.longitude != 0.0){
                            newList.add(child)
                        }
                    }
                }
                _billList.emit(newList)
            }
        }
    }

}