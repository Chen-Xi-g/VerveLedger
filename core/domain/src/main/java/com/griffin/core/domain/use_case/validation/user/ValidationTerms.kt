package com.griffin.core.domain.use_case.validation.user

/**
 * 校验条款
 */
class ValidationTerms {

    /**
     * 校验条款是否同意
     *
     * @param acceptedTerms 条款
     *
     * @return 校验结果 [ValidationResult]
     */
    fun execute(acceptedTerms: Boolean): ValidationResult {
        if (!acceptedTerms){
            return ValidationResult(
                successful = false,
                errorMessage = "请先同意条款"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}