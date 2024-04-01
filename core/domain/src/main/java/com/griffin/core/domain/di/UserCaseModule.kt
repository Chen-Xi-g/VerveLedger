package com.griffin.core.domain.di

import com.griffin.core.domain.use_case.validation.bill.ValidationBill
import com.griffin.core.domain.use_case.validation.user.ValidationCode
import com.griffin.core.domain.use_case.validation.user.ValidationEmail
import com.griffin.core.domain.use_case.validation.user.ValidationPassword
import com.griffin.core.domain.use_case.validation.user.ValidationUsername
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 用例模块绑定
 *
 * @author 高国峰
 * @date 2023/10/10-12:22
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    /**
     * 绑定邮箱校验
     */
    @Provides
    @Singleton
    fun bindValidationEmail(): ValidationEmail {
        return ValidationEmail()
    }

    /**
     * 绑定用户名校验
     */
    @Provides
    @Singleton
    fun bindValidationUsername(): ValidationUsername {
        return ValidationUsername()
    }

    /**
     * 绑定密码校验
     */
    @Provides
    @Singleton
    fun bindValidationPassword(): ValidationPassword {
        return ValidationPassword()
    }

    /**
     * 绑定验证码校验
     */
    @Provides
    @Singleton
    fun bindValidationCode(): ValidationCode {
        return ValidationCode()
    }

    /**
     * 绑定账单校验
     */
    @Provides
    @Singleton
    fun bindValidationBill(): ValidationBill {
        return ValidationBill()
    }

}