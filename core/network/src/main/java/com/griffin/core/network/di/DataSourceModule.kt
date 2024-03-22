package com.griffin.core.network.di

import com.griffin.core.network.source.CommonDataSource
import com.griffin.core.network.source.CommonDataSourceImpl
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

}