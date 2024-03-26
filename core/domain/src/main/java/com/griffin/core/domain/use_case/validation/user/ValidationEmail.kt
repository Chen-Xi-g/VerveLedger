package com.griffin.core.domain.use_case.validation.user

import android.util.Patterns

/**
 * 校验邮箱
 */
class ValidationEmail {

    /**
     * 校验邮箱格式
     *
     * @param email 邮箱
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(email: String): ValidationResult {
        if (email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "邮箱不能为空"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = "邮箱格式不正确"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}