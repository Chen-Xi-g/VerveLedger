package com.griffin.feature.login.component.activate

import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.data.model.state.LoginState
import com.griffin.core.domain.use_case.validation.user.ValidationCode
import com.griffin.core.domain.use_case.validation.user.ValidationPassword
import com.griffin.core.domain.use_case.validation.user.ValidationUsername
import com.griffin.core.network.source.CommonDataSource
import com.griffin.core.network.source.user.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ActivateViewModel @Inject constructor(
    private val commonDataSource: CommonDataSource,
    private val userDataSource: UserDataSource,
    private val validationUsername: ValidationUsername,
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
    private fun activate(username: String, code: String) {
        val usernameResult = validationUsername.execute(username)
        val codeResult = validationCode.execute(code)
        val errorList = listOf(
            usernameResult,
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
                userDataSource.activate(
                    username = username,
                    code = code,
                    uuid = _captchaImage.value.uuid
                )
            },
            message = "正在提交...", isInvokeSuccess = false, isLoadingDialog = true, errorType = ERROR_DIALOG
        ) {
            success(it.message, isDialog = true)
        }
    }

    fun refreshCode() {
        getCaptchaImage()
    }

    fun activateClick() {
        activate(uiState.value.username, uiState.value.code)
    }
}