package com.griffin.core.data.dto

import android.graphics.Bitmap
import kotlinx.serialization.Serializable

/**
 * 协议
 *
 * @param agreementContent 协议内容
 * @param agreementId 协议ID
 * @param agreementType 系统协议（1用户协议 2隐私政策）
 */
@Serializable
data class AgreementDto(
    val agreementContent: String = "",
    val agreementId: Long = 0L,
    val agreementType: String = "",
)

/**
 * 验证码图片的传输对象
 *
 * @property uuid 验证码唯一标识
 * @property img 图片
 */
@Serializable
data class CaptchaImageDto(
    val uuid: String = "",
    val img: String = ""
)