package com.griffin.core.network.source.account

import com.griffin.core.data.model.AccountModel
import com.griffin.core.data.model.Resource

interface AccountDataSource {

    /**
     * 获取账户列表
     *
     * @param type 账户类型(00: 电子账户，01：储蓄账户)不传查全部
     */
    suspend fun getAccountList(type: Int?): Resource<List<AccountModel>>

}