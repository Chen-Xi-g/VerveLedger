package com.griffin.core.network.source.account

import com.griffin.core.data.mappers.toAccountModel
import com.griffin.core.data.model.AccountModel
import com.griffin.core.data.model.Resource
import com.griffin.core.data.model.toResource
import com.griffin.core.network.api.AccountApi
import com.griffin.core.network.constant.toNetError
import javax.inject.Inject

class AccountDataSourceImpl @Inject constructor(
    private val accountApi: AccountApi
) : AccountDataSource{
    override suspend fun getAccountList(type: Int?): Resource<List<AccountModel>> {
        return try {
            accountApi.getAccountList(type).toResource {
                it.toAccountModel()
            }
        } catch (e: Exception) {
            e.toNetError()
        }
    }
}