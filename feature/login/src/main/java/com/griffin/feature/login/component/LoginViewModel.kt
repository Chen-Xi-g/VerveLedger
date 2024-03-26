package com.griffin.feature.login.component

import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.domain.use_case.validation.user.ValidationCode
import com.griffin.core.domain.use_case.validation.user.ValidationPassword
import com.griffin.core.domain.use_case.validation.user.ValidationResult
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
class LoginViewModel @Inject constructor(
    private val commonDataSource: CommonDataSource,
    private val userDataSource: UserDataSource,
    private val validationUsername: ValidationUsername,
    private val validationPassword: ValidationPassword,
    private val validationCode: ValidationCode
) : BaseViewModel() {

    /**
     * 图片验证码
     */
    private val _captchaImage = MutableStateFlow(CaptchaImageModel())

    /**
     * 登录状态
     */
    private val _loginState = MutableSharedFlow<Boolean>()

    /**
     * 合并图片验证码和登录状态
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val flow = flowOf(_captchaImage, _loginState)
        .flatMapMerge { it }

    /**
     * 获取图形验证码
     */
    fun getCaptchaImage() {
        handleRequest({ commonDataSource.captchaImage() }, isLoadingDialog = null) {
            _captchaImage.value = it.`data` ?: CaptchaImageModel()
        }
    }

    /**
     * 登录账户
     */
    fun login(username: String, password: String, code: String) {
        val usernameResult = validationUsername.execute(username)
        val passwordResult = validationPassword.execute(password)
        val codeResult = validationCode.execute(code)
        val errorList = listOf(
            usernameResult,
            passwordResult,
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
                userDataSource.login(
                    username = username,
                    password = password,
                    code = code,
                    uuid = _captchaImage.value.uuid
                )
            },
            message = "登录中...", isLoadingDialog = true, errorType = ERROR_DIALOG
        ) {
            // 登录成功
            BaseMV.User.token = it.data ?: ""
            emit {
                _loginState.emit(true)
            }
        }
    }

}