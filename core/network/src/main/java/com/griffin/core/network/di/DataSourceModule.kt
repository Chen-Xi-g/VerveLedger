package com.griffin.core.network.di

import com.griffin.core.network.source.CommonDataSource
import com.griffin.core.network.source.CommonDataSourceImpl
import com.griffin.core.network.source.account.AccountDataSource
import com.griffin.core.network.source.account.AccountDataSourceImpl
import com.griffin.core.network.source.bill.BillDataSource
import com.griffin.core.network.source.bill.BillDataSourceImpl
import com.griffin.core.network.source.type.TypeDataSource
import com.griffin.core.network.source.type.TypeDataSourceImpl
import com.griffin.core.network.source.user.UserDataSource
import com.griffin.core.network.source.user.UserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    /**
     * 绑定通用数据仓库
     */
    @Binds
    @Singleton
    abstract fun bindCommonRepository(commonDataSourceImpl: CommonDataSourceImpl): CommonDataSource

    /**
     * 绑定用户数据仓库
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    /**
     * 绑定账单数据仓库
     */
    @Binds
    @Singleton
    abstract fun bindBillRepository(billDataSourceImpl: BillDataSourceImpl): BillDataSource

    /**
     * 绑定类型数据仓库
     */
    @Binds
    @Singleton
    abstract fun bindTypeRepository(typeDataSourceImpl: TypeDataSourceImpl): TypeDataSource

    /**
     * 绑定账户数据仓库
     */
    @Binds
    @Singleton
    abstract fun bindAccountRepository(accountDataSourceImpl: AccountDataSourceImpl): AccountDataSource

}