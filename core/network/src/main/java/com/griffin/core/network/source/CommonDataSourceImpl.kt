package com.griffin.core.network.source

import com.griffin.core.data.mappers.toCaptchaImageModel
import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.data.model.Resource
import com.griffin.core.data.model.toResource
import com.griffin.core.network.api.CommonApi
import com.griffin.core.network.constant.toNetError
import javax.inject.Inject

/**
 * 通用数据仓库实现
 */
class CommonDataSourceImpl @Inject constructor(
    private val commonApi: CommonApi
) : CommonDataSource {

    override suspend fun agreement(type: Int): Resource<String> {
       return try {
           commonApi.getProtocolContent(type).toResource{
               it?.agreementContent ?: ""
           }
       }catch (e: Exception){
           e.toNetError()
       }
    }

    override suspend fun captchaImage(): Resource<CaptchaImageModel> {
        return try {
            commonApi.captchaImage().toResource{
                it.toCaptchaImageModel()
            }
        }catch (e: Exception){
            e.toNetError()
        }
    }

}