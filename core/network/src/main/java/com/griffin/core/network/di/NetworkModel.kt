package com.griffin.core.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.griffin.core.network.BuildConfig
import com.griffin.core.network.api.CommonApi
import com.griffin.core.network.constant.NetworkConstant
import com.griffin.core.network.interceptor.ParameterInterceptor
import com.griffin.core.network.interceptor.ResponseInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
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

    /**
     * 提供网络请求的OkHttp
     */
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

    /**
     * 提供通用Api的Retrofit
     */
    @Provides
    @Singleton
    fun provideCommonRetrofit(callFactory: Call.Factory, json: Json): CommonApi = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL + NetworkConstant.BASE_URL)
        .callFactory(callFactory)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create()

}