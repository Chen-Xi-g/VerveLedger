package com.griffin.core.domain.use_case.validation.user

/**
 * 校验返回结果
 *
 * @param successful 是否成功
 * @param errorMessage 错误信息
 */
data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)