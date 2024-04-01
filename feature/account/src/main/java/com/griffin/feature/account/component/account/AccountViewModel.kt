package com.griffin.feature.account.component.account

import androidx.lifecycle.viewModelScope
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.AccountModel
import com.griffin.core.network.source.account.AccountDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountDataSource: AccountDataSource
) : BaseViewModel() {

    /**
     * 账户信息列表
     */
    private val _list = MutableSharedFlow<List<AccountModel>>()
    val list = _list.asSharedFlow()

    /**
     * 获取账户列表
     */
    fun accountList() {
        handleRequest(
            block = {
                accountDataSource.getAccountList(null)
            }
        ) { list ->
            viewModelScope.launch {
                _list.emit(list.data ?: emptyList())
            }
        }
    }

}