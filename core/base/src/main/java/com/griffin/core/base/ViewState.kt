package com.griffin.core.base

sealed class ViewState(
    val isDialog: Boolean = false,
    val isLayout: Boolean = false,
    val isEmpty: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val msg: String? = null,
    val code: Int? = null,
    val url: String? = null
) {
    /**
     * 加载中弹窗
     *
     * @param msgInfo 加载中信息
     */
    data class LoadingDialog(private val msgInfo: String?) :
        ViewState(isDialog = true, msg = msgInfo)

    /**
     * 加载中布局
     *
     * @param msgInfo 加载中信息
     */
    data class LoadingLayout(private val msgInfo: String?) :
        ViewState(isLayout = true, msg = msgInfo)

    /**
     * 空数据
     *
     * @param msgInfo 空数据信息
     */
    data class Empty(private val msgInfo: String?) : ViewState(isEmpty = true, msg = msgInfo)

    /**
     * 加载失败回调
     *
     * @param msgInfo 错误信息
     * @param isToast 是否弹出toast
     * @param codeInfo 错误码
     * @param urlInfo 错误地址
     */
    data class Error(
        private val msgInfo: String?,
        private val isToast: Boolean = false,
        private val codeInfo: Int = 0,
        private val urlInfo: String? = null
    ) : ViewState(isError = true, msg = msgInfo, code = codeInfo, url = urlInfo, isDialog = isToast)

    /**
     * 加载成功
     */
    data object Success : ViewState(isSuccess = true, code = 200)
}