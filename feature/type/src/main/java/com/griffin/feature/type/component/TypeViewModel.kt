package com.griffin.feature.type.component

import androidx.lifecycle.viewModelScope
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.TypeModel
import com.griffin.core.network.source.type.TypeDataSource
import com.griffin.core.rv.RvConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeViewModel @Inject constructor(
    private val typeDataSource: TypeDataSource
) : BaseViewModel() {

    private val _typeList = MutableSharedFlow<List<TypeModel>>()
    val typeList = _typeList.asSharedFlow()

    /**
     * 加载数据
     *
     * @param typeTag 类型标签（0：支出，1：收入）
     */
    fun load(typeTag: Int){
        handleRequest(
            block = {
                typeDataSource.getPayTypeAndChild(typeTag.toString())
            }
        ){
            viewModelScope.launch {
                _typeList.emit(it.data ?: emptyList())
            }
        }
    }

}