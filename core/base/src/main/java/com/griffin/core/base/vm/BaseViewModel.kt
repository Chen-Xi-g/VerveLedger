package com.griffin.core.base.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griffin.core.base.ViewState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel基类
 */
open class BaseViewModel : ViewModel() {

    /**
     * UI状态
     */
    private val _viewState = MutableSharedFlow<ViewState>()
    val viewState: SharedFlow<ViewState> = _viewState.asSharedFlow()

    /**
     * 显示加载中弹窗
     *
     * @param message 加载中弹窗的提示信息
     */
    fun loadingDialog(message: String? = "加载中...") {
        viewModelScope.launch {
            _viewState.emit(ViewState.LoadingDialog(message))
        }
    }

    /**
     * 显示加载中布局
     *
     * @param message 加载中布局的提示信息
     */
    fun loadingLayout(message: String? = "加载中...") {
        viewModelScope.launch {
            _viewState.emit(ViewState.LoadingLayout(message))
        }
    }

    /**
     * 显示空数据
     *
     * @param message 空数据的提示信息
     */
    fun empty(message: String? = "- 暂无数据 -") {
        viewModelScope.launch {
            _viewState.emit(ViewState.Empty(message))
        }
    }

    /**
     * 显示加载失败
     *
     * @param message 加载失败的提示信息
     * @param isToast 是否弹出toast
     * @param code 加载失败的错误码
     * @param url 加载失败的地址
     */
    fun error(
        message: String? = "加载失败",
        isToast: Boolean = false,
        code: Int = 0,
        url: String? = null
    ) {
        viewModelScope.launch {
            _viewState.emit(ViewState.Error(message, isToast, code, url))
        }
    }

    /**
     * 显示加载成功
     */
    fun success() {
        viewModelScope.launch {
            _viewState.emit(ViewState.Success)
        }
    }
}