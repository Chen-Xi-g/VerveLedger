package com.griffin.core.network.source

import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.data.model.Resource

/**
 * 通用数据仓库
 */
interface CommonDataSource {
    /**
     * 获取协议内容
     *
     * @param type 类型（1：用户协议 2：隐私协议）
     *
     * @return 协议内容
     */
    suspend fun agreement(type: Int): Resource<String>

    /**
     * 获取图形验证码
     */
    suspend fun captchaImage(): Resource<CaptchaImageModel>
}