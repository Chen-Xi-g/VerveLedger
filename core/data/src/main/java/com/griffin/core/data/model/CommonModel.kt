package com.griffin.core.data.model

import android.graphics.Bitmap

/**
 * 验证码图片的页面数据
 *
 * @property bitmap 图片
 * @property uuid 验证码唯一标识
 */
data class CaptchaImageModel(
    val bitmap: Bitmap? = null,
    val uuid: String = ""
)