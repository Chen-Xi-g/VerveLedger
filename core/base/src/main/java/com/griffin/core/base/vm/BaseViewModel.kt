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

    companion object{
        /**
         * 什么都不显示
         */
        const val ERROR_NOTING = 0

        /**
         * 显示Toast
         */
        const val ERROR_TOAST = 1

        /**
         * 显示Dialog
         */
        const val ERROR_DIALOG = 2

        /**
         * 显示布局
         */
        const val ERROR_LAYOUT = 3
    }

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
     * @param isDialog 是否弹出dialog
     * @param code 加载失败的错误码
     */
    protected fun error(
        message: String? = "加载失败",
        isToast: Boolean = false,
        isDialog: Boolean = false,
        code: Int = 0
    ) {
        viewModelScope.launch {
            if (isDialog){
                _viewState.emit(ViewState.ErrorDialog(message, isToast, code))
            }else{
                _viewState.emit(ViewState.ErrorLayout(message, isToast, code))
            }
        }
    }

    /**
     * 显示加载成功
     *
     * @param message 加载成功的提示信息
     * @param isDialog 是否弹出toast
     */
    protected fun success(
        message: String? = null,
        isDialog: Boolean = false
    ) {
        viewModelScope.launch {
            _viewState.emit(ViewState.Success(isDialog = isDialog, msgInfo = message))
        }
    }

    protected fun emit(block: suspend () -> Unit){
        viewModelScope.launch {
            block()
        }
    }

    /**
     * 处理请求
     *
     * @param block 请求的代码块
     * @param message 请求中的提示信息
     * @param isInvokeSuccess 是否调用成功的回调
     * @param isLoadingDialog 是否显示加载中弹窗（null: 都不显示, true：显示Dialog，false：显示布局）
     * @param errorType 错误类型（0：什么都不显示，1：显示Toast，2：显示Dialog，3：显示布局）
     * @param error 请求失败的回调
     * @param success 请求成功的回调
     */
    protected fun <T> handleRequest(
        block: suspend () -> Resource<T>,
        message: String? = "加载中...",
        isInvokeSuccess: Boolean = true,
        isLoadingDialog: Boolean? = null,
        errorType: Int = ERROR_TOAST,
        error: ((Resource.Error<T>) -> Unit)? = null,
        success: (Resource.Success<T>) -> Unit
    ) {
        if (isLoadingDialog == true){
            loadingDialog(message)
        } else if (isLoadingDialog == false){
            loadingLayout(message)
        }
        viewModelScope.launch {
            when (val result = block()) {
                is Resource.Success -> {
                    success.invoke(result)
                    if (isInvokeSuccess){
                        this@BaseViewModel.success()
                    }
                }

                is Resource.Error -> {
                    error?.invoke(result)
                    when(errorType){
                        1 -> {
                            error(result.message, isToast = true, isDialog = false, code = result.code)
                        }
                        2 -> {
                            error(result.message, isToast = false, isDialog = true, code = result.code)
                        }
                        3 -> {
                            error(result.message, isToast = false, isDialog = false, code = result.code)
                        }
                        else -> {
                            success()
                        }
                    }
                }
            }
        }
    }
}