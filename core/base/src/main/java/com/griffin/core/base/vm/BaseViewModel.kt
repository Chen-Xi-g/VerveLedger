package com.griffin.core.base.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griffin.core.base.ViewState
import com.griffin.core.data.model.Resource
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
    protected fun loadingDialog(message: String? = "加载中...") {
        viewModelScope.launch {
            _viewState.emit(ViewState.LoadingDialog(message))
        }
    }

    /**
     * 显示加载中布局
     *
     * @param message 加载中布局的提示信息
     */
    protected fun loadingLayout(message: String? = "加载中...") {
        viewModelScope.launch {
            _viewState.emit(ViewState.LoadingLayout(message))
        }
    }

    /**
     * 显示空数据
     *
     * @param message 空数据的提示信息
     */
    protected fun empty(message: String? = "- 暂无数据 -") {
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
     */
    protected fun error(
        message: String? = "加载失败",
        isToast: Boolean = false,
        code: Int = 0
    ) {
        viewModelScope.launch {
            _viewState.emit(ViewState.Error(message, isToast, code))
        }
    }

    /**
     * 显示加载成功
     */
    protected fun success() {
        viewModelScope.launch {
            _viewState.emit(ViewState.Success)
        }
    }

    /**
     * 处理请求
     *
     * @param block 请求的代码块
     * @param message 请求中的提示信息
     * @param isDialog 是否显示加载中弹窗（null: 都不显示, true：显示Dialog，false：显示布局）
     * @param success 请求成功的回调
     */
    protected fun <T> handleRequest(
        block: suspend () -> Resource<T>,
        message: String? = "加载中...",
        isDialog: Boolean? = false,
        success: (T) -> Unit
    ) {
        if (isDialog == true){
            loadingDialog(message)
        } else if (isDialog == false){
            loadingLayout(message)
        }
        viewModelScope.launch {
            when (val result = block()) {
                is Resource.Success -> {
                    result.`data`?.let(success)
                    this@BaseViewModel.success()
                }

                is Resource.Error -> error(result.message, false, result.code)
            }
        }
    }
}