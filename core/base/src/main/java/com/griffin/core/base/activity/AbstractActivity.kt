package com.griffin.core.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity抽象类
 */
abstract class AbstractActivity : AppCompatActivity(){

    /**
     * 实现initView来做视图相关的初始化
     */
    abstract fun initView(savedInstanceState: Bundle?)

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
     * 设置布局内边距+标题栏内边距
     */
    open fun paddingWindow() {}

    /**
     * 错误回调
     *
     * @param message 错误信息
     * @param isToast 是否弹出toast
     * @param code 错误码
     */
    open fun onError(message: String?,isToast: Boolean, code: Int?) {}

}
