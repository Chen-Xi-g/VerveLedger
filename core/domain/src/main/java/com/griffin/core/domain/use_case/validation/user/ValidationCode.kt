package com.griffin.core.domain.use_case.validation.user

/**
 * 校验密码
 */
class ValidationCode {

    /**
     * 校验邮箱格式
     *
     * @param code 验证码
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(code: String): ValidationResult {
        if (code.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "验证码不能为空"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}