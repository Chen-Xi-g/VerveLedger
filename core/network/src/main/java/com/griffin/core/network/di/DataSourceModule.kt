package com.griffin.core.network.di

import com.griffin.core.network.source.CommonDataSource
import com.griffin.core.network.source.CommonDataSourceImpl
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

}