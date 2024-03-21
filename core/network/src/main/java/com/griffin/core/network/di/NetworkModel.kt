package com.griffin.core.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.griffin.core.network.interceptor.ParameterInterceptor
import com.griffin.core.network.interceptor.ResponseInterceptor
import com.therouter.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * 使用Hilt提供网络请求
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModel {

    /**
     * 提供网络请求的Json解析器
     */
    @Provides
    @Singleton
    fun provideNetworkJson(): Json = Json{
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(@ApplicationContext context: Context): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .addInterceptor(ParameterInterceptor())
        .addInterceptor(ResponseInterceptor())
        .addInterceptor(
            ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()
        )
        .build()

}