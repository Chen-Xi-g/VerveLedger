package com.griffin.core.network.api

import com.griffin.core.data.dto.AgreementDto
import com.griffin.core.data.dto.BaseDto
import com.griffin.core.data.dto.CaptchaImageDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 网络请求接口
 */
interface CommonApi {

    /**
     * 获取协议内容
     *
     * @param type 类型（1：用户协议 2：隐私协议）
     */
    @GET("agreement")
    suspend fun getProtocolContent(@Query("type") type: Int): BaseDto<AgreementDto>

    /**
     * 获取图形验证码
     *
     * @return Net<CaptchaImageVo> 图形验证码实体类
     */
    @GET("captchaImage")
    suspend fun captchaImage(): BaseDto<CaptchaImageDto>

}