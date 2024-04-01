package com.griffin.feature.login.component.register

import androidx.lifecycle.viewModelScope
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.data.model.state.LoginState
import com.griffin.core.domain.use_case.validation.user.ValidationCode
import com.griffin.core.domain.use_case.validation.user.ValidationEmail
import com.griffin.core.domain.use_case.validation.user.ValidationPassword
import com.griffin.core.domain.use_case.validation.user.ValidationUsername
import com.griffin.core.network.source.CommonDataSource
import com.griffin.core.network.source.user.UserDataSource
import com.griffin.core.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 注册ViewModel
 *
 * @param commonDataSource 通用数据源
 * @param userDataSource 用户数据源
 * @param validationUsername 用户名校验
 * @param validationPassword 密码校验
 * @param validationEmail 邮箱校验
 * @param validationCode 验证码校验
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val commonDataSource: CommonDataSource,
    private val userDataSource: UserDataSource,
    private val validationUsername: ValidationUsername,
    private val validationPassword: ValidationPassword,
    private val validationEmail: ValidationEmail,
    private val validationCode: ValidationCode
) : BaseViewModel() {

    /**
     * 页面状态
     */
    val uiState = MutableStateFlow(LoginState())

    /**
     * 图片验证码
     */
    private val _captchaImage = MutableStateFlow(CaptchaImageModel())

    /**
     * 注册是否成功
     */
    private val _registerSuccess = MutableSharedFlow<Boolean>()

    /**
     * 图片验证码和注册是否成功
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val flow = flowOf(_captchaImage, _registerSuccess)
        .flatMapMerge { it }

    /**
     * 获取验证码
     */
    private fun getCaptchaImage() {
        handleRequest({ commonDataSource.captchaImage() }, isLoadingDialog = null) {
            _captchaImage.value = it.data ?: CaptchaImageModel()
        }
    }

    /**
     * 注册账号
     *
     * @param code 验证码内容
     * @param email 邮箱
     * @param password 密码
     * @param confirmPassword 确认密码
     * @param username 用户名
     */
    private fun register(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
        username: String
    ) {
        // 校验输入内容
        val usernameResult = validationUsername.execute(username)
        val emailResult = validationEmail.execute(email)
        val passwordResult = validationPassword.execute(password)
        val confirmPasswordResult = validationPassword.execute(password, confirmPassword)
        val codeResult = validationCode.execute(code)
        val errorList = listOf(
            usernameResult,
            emailResult,
            passwordResult,
            confirmPasswordResult,
            codeResult
        )
        if (errorList.any { !it.successful }) {
            error(message = errorList.first { !it.successful }.errorMessage, isToast = true)
            return
        }
        handleRequest(
            block = {
                userDataSource.register(
                    code,
                    email,
                    password,
                    username,
                    _captchaImage.value.uuid
                )
            },
            message = "注册中...", isLoadingDialog = true, errorType = ERROR_DIALOG
        ) {
            it.message?.toast()
            emit {
                _registerSuccess.emit(true)
            }
        }
    }

    fun registerClick(){
        register(uiState.value.code, uiState.value.email, uiState.value.password, uiState.value.confirmPassword, uiState.value.username)
    }

    fun refreshCode(){
        getCaptchaImage()
    }
}