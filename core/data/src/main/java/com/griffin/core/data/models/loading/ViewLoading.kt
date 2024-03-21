package com.griffin.core.data.models.loading

/**
 * View加载状态
 *
 * @property showDialog 是否加载中（ture：显示加载弹窗，false：隐藏加载弹窗）
 * @property message 加载提示信息
 */
data class ViewLoading(
    val showDialog: Boolean = false,
    val message: String? = null
)