package com.griffin.feature.map.component

import androidx.lifecycle.viewModelScope
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiSearchV2
import com.griffin.core.base.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : BaseViewModel() {

    /**
     * 回调搜索数据
     */
    private val _tipList = MutableSharedFlow<List<Tip>>()
    val tipList = _tipList.asSharedFlow()

    /**
     * 当前选择的位置
     */
    private val _tip = MutableSharedFlow<Tip>()
    val tip = _tip.asSharedFlow()

    /**
     * 搜索结果回调
     */
    val inputListener = Inputtips.InputtipsListener { list, p1 ->
        viewModelScope.launch {
            _tipList.emit(list ?: emptyList())
        }
    }

    /**
     * 更新当前选择的位置
     */
    fun updateCurrentTip(tip: Tip){
        viewModelScope.launch {
            _tip.emit(tip)
        }
    }

}