package com.griffin.core.base

sealed class ViewState(
    val isToast: Boolean = false,
    val msg: String? = null,
    val code: Int? = null
) {
    /**
     * 加载中弹窗
     *
     * @param msgInfo 加载中信息
     */
    data class LoadingDialog(private val msgInfo: String?) :
        ViewState(msg = msgInfo)

    /**
     * 加载中布局
     *
     * @param msgInfo 加载中信息
     */
    data class LoadingLayout(private val msgInfo: String?) :
        ViewState(msg = msgInfo)

    /**
     * 空数据
     *
     * @param msgInfo 空数据信息
     */
    data class Empty(private val msgInfo: String?) : ViewState(msg = msgInfo)

    /**
     * 加载失败回调
     *
     * @param msgInfo 错误信息
     * @param showToast 是否弹出toast
     * @param codeInfo 错误码
     * @param urlInfo 错误地址
     */
    data class Error(
        private val msgInfo: String?,
        private val showToast: Boolean = false,
        private val codeInfo: Int = 0
    ) : ViewState(msg = msgInfo, code = codeInfo, isToast = showToast)

    /**
     * 加载成功
     */
    data object Success : ViewState(code = 200)
}