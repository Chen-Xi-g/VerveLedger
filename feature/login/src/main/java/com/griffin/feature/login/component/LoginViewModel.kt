package com.griffin.feature.login.component

import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.network.source.CommonDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val commonDataSource: CommonDataSource
) : BaseViewModel() {

    /**
     * 图片验证码
     */
    private val _captchaImage = MutableStateFlow(CaptchaImageModel())
    val captchaImage: StateFlow<CaptchaImageModel> = _captchaImage.asStateFlow()

    /**
     * 获取图形验证码
     */
    fun getCaptchaImage() {
        handleRequest({ commonDataSource.captchaImage() },isDialog = null) {
            _captchaImage.value = it
        }
    }

}