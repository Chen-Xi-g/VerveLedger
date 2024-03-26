package com.griffin.core.base.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.griffin.core.base.R

abstract class AbstractFragment : Fragment(R.layout.base_root_layout){

    /**
     * 实现initView来做视图相关的初始化
     */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 实现obtainData来做数据的初始化
     */
    abstract fun obtainData()

    /**
     * 注册流对象观察
     */
    abstract fun registerObserver()

    /**
     * 重试
     */
    open fun onRetry() {}

    /**
     * 是否显示标题栏，默认不显示
     */
    open fun showTitleBar() = false

    /**
     * 错误回调
     *
     * @param message 错误信息
     * @param isToast 是否弹出toast
     * @param isDialog 是否弹出dialog
     * @param code 错误码
     */
    open fun onError(message: String?,isToast: Boolean, isDialog: Boolean, code: Int?) {}

}