package com.griffin.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ViewBindingUtil
import com.griffin.core.base.ViewState
import com.griffin.core.base.databinding.BaseRootLayoutBinding
import com.griffin.core.base.dialog.LoadingDialog
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.utils.mmkv.toast
import com.therouter.TheRouter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : AbstractFragment() {

    /**
     * 根布局
     */
    private lateinit var _rootBinding: BaseRootLayoutBinding
    val rootBinding get() = _rootBinding

    /**
     * 泛型中的ViewBinding实例
     */
    private lateinit var _binding: VB
    val binding get() = _binding

    /**
     * 获取泛型中的ViewModel实例
     */
    private lateinit var _viewModel: VM
    val viewModel get() = _viewModel

    /**
     * 加载中弹窗
     */
    val loadingDialog by lazy {
        LoadingDialog(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheRouter.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewWithBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel = createViewModel()
        registerObserver()
        initClick()
        _rootBinding.baseLoadingLayout.root.showAnim()
        initView(view, savedInstanceState)
        obtainData()
    }

    private fun createViewWithBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        _rootBinding = BaseRootLayoutBinding.inflate(inflater, container, false)
        if (showTitleBar()) {
            _rootBinding.baseTitleLayout.root.showAnim()
        } else {
            _rootBinding.baseTitleLayout.root.hide()
        }
        initContent()
        return _rootBinding.root
    }

    private fun initContent() {
        _binding = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
        _rootBinding.baseContentLayout.removeAllViews()
        _rootBinding.baseContentLayout.addView(_binding.root)
    }

    private fun initClick() {
        _rootBinding.baseErrorLayout.root.setOnClickListener {
            if (_rootBinding.baseErrorLayout.root.visibility == View.VISIBLE) {
                onRetry()
            }
        }
    }

    /**
     * 注册状态
     */
    override fun registerObserver() {
        lifecycleScope.launch {
            _viewModel.viewState.collectLatest {
                // 监听UI状态
                when (it) {
                    is ViewState.Empty -> showEmpty(it.msg)
                    is ViewState.Error -> showError(it.msg)
                    is ViewState.LoadingDialog -> showLoadingDialog(it.msg)
                    is ViewState.LoadingLayout -> showLoading(it.msg)
                    ViewState.Success -> showContent()
                }
            }
        }
    }

    /**
     * 显示成功内容
     */
    fun showContent() {
        loadingDialog.dismiss()
        _rootBinding.baseLoadingLayout.root.hide()
        _rootBinding.baseEmptyLayout.root.hide()
        _rootBinding.baseErrorLayout.root.hide()
        _rootBinding.baseContentLayout.showAnim()
    }

    /**
     * 显示加载中布局
     */
    fun showLoading(msg: String? = null) {
        if (!msg.isNullOrEmpty()) {
            _rootBinding.baseLoadingLayout.baseLoadingText.text = msg
        }
        loadingDialog.dismiss()
        _rootBinding.baseLoadingLayout.root.showAnim()
        _rootBinding.baseEmptyLayout.root.hide()
        _rootBinding.baseErrorLayout.root.hide()
        _rootBinding.baseContentLayout.hide()
    }

    /**
     * 显示加载弹窗
     *
     * @param msg 提示信息
     */
    fun showLoadingDialog(msg: String? = null) {
        if (!msg.isNullOrEmpty()) {
            loadingDialog.updateText(msg)
        }
        loadingDialog.show()
        _rootBinding.baseLoadingLayout.root.hide()
        _rootBinding.baseEmptyLayout.root.hide()
        _rootBinding.baseErrorLayout.root.hide()
        _rootBinding.baseContentLayout.hide()
    }

    /**
     * 显示空数据布局
     *
     * @param msg 提示信息
     */
    fun showEmpty(msg: String? = null) {
        if (!msg.isNullOrEmpty()) {
            _rootBinding.baseEmptyLayout.baseEmptyText.text = msg
        }
        loadingDialog.dismiss()
        _rootBinding.baseLoadingLayout.root.hide()
        _rootBinding.baseEmptyLayout.root.showAnim()
        _rootBinding.baseErrorLayout.root.hide()
        _rootBinding.baseContentLayout.hide()
    }

    /**
     * 显示加载失败布局
     *
     * @param msg 错误信息
     */
    fun showError(msg: String? = null) {
        _rootBinding.baseErrorLayout.baseLoadingText.text = msg
        loadingDialog.dismiss()
        _rootBinding.baseLoadingLayout.root.hide()
        _rootBinding.baseEmptyLayout.root.hide()
        _rootBinding.baseErrorLayout.root.showAnim()
        _rootBinding.baseContentLayout.hide()
    }

    /**
     * 设置标题名称
     */
    fun setTitleName(title: String) {
        _rootBinding.baseTitleLayout.baseTitleText.text = title
    }

    /**
     * 显示View的动画
     */
    fun View.showAnim(duration: Long = 400L) {
        if (visibility == View.VISIBLE) {
            return
        }
        visibility = View.VISIBLE
        alpha = 0f
        animate().setDuration(duration).alpha(1f).start()
    }

    /**
     * 隐藏View
     */
    fun View.hide() {
        if (visibility != View.VISIBLE) {
            return
        }
        visibility = View.INVISIBLE
    }

    override fun onError(message: String, isToast: Boolean, code: Int, url: String) {
        if (isToast) {
            // 弹出toast
            message.toast()
        } else {
            // 显示错误布局
            showError(message)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <VM> getVmClazz(obj: Any): VM {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }
}