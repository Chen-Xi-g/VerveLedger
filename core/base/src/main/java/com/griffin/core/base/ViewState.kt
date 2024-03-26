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
     * 加载失败布局回调
     *
     * @param msgInfo 错误信息
     * @param showToast 是否弹出toast
     * @param codeInfo 错误码
     */
    data class ErrorLayout(
        private val msgInfo: String?,
        private val showToast: Boolean = false,
        private val codeInfo: Int = 0
    ) : ViewState(msg = msgInfo, code = codeInfo, isToast = showToast)

    /**
     * 加载失败弹窗回调
     *
     * @param msgInfo 错误信息
     * @param showToast 是否弹出toast
     * @param codeInfo 错误码
     */
    data class ErrorDialog(
        private val msgInfo: String?,
        private val showToast: Boolean = false,
        private val codeInfo: Int = 0
    ) : ViewState(msg = msgInfo, code = codeInfo, isToast = showToast)

    /**
     * 加载成功
     *
     * @param isDialog 是否弹出dialog
     * @param msgInfo 成功信息
     */
    data class Success(
        private val isDialog: Boolean = false,
        private val msgInfo: String? = null
    ) : ViewState(code = 200, isToast = isDialog, msg = msgInfo)
}