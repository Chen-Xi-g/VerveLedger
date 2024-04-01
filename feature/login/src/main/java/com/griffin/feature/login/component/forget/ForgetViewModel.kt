package com.griffin.feature.login.component.forget

import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.data.model.state.LoginState
import com.griffin.core.domain.use_case.validation.user.ValidationCode
import com.griffin.core.domain.use_case.validation.user.ValidationPassword
import com.griffin.core.domain.use_case.validation.user.ValidationUsername
import com.griffin.core.network.source.CommonDataSource
import com.griffin.core.network.source.user.UserDataSource
import com.griffin.core.utils.mmkv.BaseMV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class ForgetViewModel @Inject constructor(
    private val commonDataSource: CommonDataSource,
    private val userDataSource: UserDataSource,
    private val validationUsername: ValidationUsername,
    private val validationPassword: ValidationPassword,
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
    val captchaImage: StateFlow<CaptchaImageModel> = _captchaImage.asStateFlow()

    /**
     * 获取图形验证码
     */
    private fun getCaptchaImage() {
        handleRequest({ commonDataSource.captchaImage() }, isLoadingDialog = null) {
            _captchaImage.value = it.data ?: CaptchaImageModel()
        }
    }

    /**
     * 找回密码
     */
    private fun forgetPwd(username: String, password: String, confirmPassword: String, code: String) {
        val usernameResult = validationUsername.execute(username)
        val passwordResult = validationPassword.execute(password)
        val confirmPasswordResult = validationPassword.execute(password, confirmPassword)
        val codeResult = validationCode.execute(code)
        val errorList = listOf(
            usernameResult,
            passwordResult,
            confirmPasswordResult,
            codeResult
        )
        if (errorList.any { !it.successful }) {
            error(
                message = errorList.first { !it.successful }.errorMessage,
                isToast = true
            )
            return
        }
        handleRequest(
            block = {
                userDataSource.forgetPassword(
                    username = username,
                    password = password,
                    confirmPassword = confirmPassword,
                    code = code,
                    uuid = _captchaImage.value.uuid
                )
            },
            message = "正在提交...", isInvokeSuccess = false, isLoadingDialog = true, errorType = ERROR_DIALOG
        ) {
            success(it.message, isDialog = true)
        }
    }

    fun forgetPwdClick() {
        forgetPwd(uiState.value.username, uiState.value.password, uiState.value.confirmPassword, uiState.value.code)
    }

    fun refreshCode() {
        getCaptchaImage()
    }

}