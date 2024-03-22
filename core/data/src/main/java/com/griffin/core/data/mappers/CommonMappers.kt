package com.griffin.core.data.mappers

import android.graphics.BitmapFactory
import android.util.Base64
import com.griffin.core.data.dto.CaptchaImageDto
import com.griffin.core.data.model.CaptchaImageModel

/**
 * 验证码图片的Dto数据转换为实体类数据
 */
fun CaptchaImageDto?.toCaptchaImageModel(): CaptchaImageModel {
    if (this == null) {
        return CaptchaImageModel()
    }
    val decodedByte = Base64.decode(img, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    return CaptchaImageModel(bitmap, uuid)
}