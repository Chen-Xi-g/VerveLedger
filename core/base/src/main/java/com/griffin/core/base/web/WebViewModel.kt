package com.griffin.core.base.web

import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.network.source.CommonDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * WebViewModel
 *
 * @property commonDataSource 通用数据仓库
 */
@HiltViewModel
class WebViewModel @Inject constructor(
    private val commonDataSource: CommonDataSource
) : BaseViewModel() {

    /**
     * 协议内容
     */
    private val _agreementContent = MutableStateFlow("")
    val agreementContent: StateFlow<String> = _agreementContent.asStateFlow()

    /**
     * 获取协议内容
     *
     * @param type 类型（1：用户协议 2：隐私协议）
     */
    fun getAgreementContent(type: Int) {
        handleRequest({ commonDataSource.agreement(type) }) {
            _agreementContent.value = it
        }
    }

}