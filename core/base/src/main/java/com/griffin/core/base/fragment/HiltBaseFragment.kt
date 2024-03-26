package com.griffin.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.R
import com.griffin.core.base.ViewState
import com.griffin.core.base.databinding.BaseRootLayoutBinding
import com.griffin.core.base.dialog.LoadingDialog
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.dialog.ErrorDialog
import com.griffin.core.dialog.SuccessDialog
import com.griffin.core.utils.gone
import com.griffin.core.utils.toast
import com.griffin.core.utils.visible
import com.therouter.TheRouter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * 用于实现Hilt的ViewModel
 */
abstract class HiltBaseFragment<DB : ViewDataBinding>(@LayoutRes val layoutResId: Int) : AbstractFragment() {

    /**
     * 根布局
     */
    private lateinit var _rootBinding: BaseRootLayoutBinding
    val rootBinding get() = _rootBinding

    /**
     * 泛型中的ViewBinding实例
     */
    private lateinit var _binding: DB
    val binding get() = _binding

    /**
     * 获取泛型中的ViewModel实例
     */
    abstract val viewModel: BaseViewModel

    /**
     * 加载中弹窗
     */
    val loadingDialog by lazy {
        LoadingDialog(requireContext())
    }

    /**
     * 错误弹窗
     */
    val errorDialog by lazy {
        ErrorDialog(requireContext())
    }

    /**
     * 成功弹窗
     */
    val successDialog by lazy {
        SuccessDialog(requireContext())
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
        _rootBinding = DataBindingUtil.inflate(inflater, R.layout.base_root_layout, container, false)
        _rootBinding.lifecycleOwner = this
        if (showTitleBar()) {
            _rootBinding.baseTitleLayout.root.visible()
        } else {
            _rootBinding.baseTitleLayout.root.gone()
        }
        initContent()
        return _rootBinding.root
    }

    private fun initContent() {
        _rootBinding.baseContentLayout.removeAllViews()
        _binding = DataBindingUtil.inflate(layoutInflater, layoutResId, _rootBinding.baseContentLayout, true)
        _binding.lifecycleOwner = this
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
            viewModel.viewState.collectLatest {
                // 监听UI状态
                when (it) {
                    is ViewState.Empty -> showEmpty(it.msg)
                    is ViewState.ErrorLayout -> {
                        onError(it.msg, it.isToast,false, it.code)
                    }
                    is ViewState.ErrorDialog -> {
                        onError(it.msg, it.isToast, true, it.code)
                    }
                    is ViewState.LoadingDialog -> showLoadingDialog(it.msg)
                    is ViewState.LoadingLayout -> showLoading(it.msg)
                    is ViewState.Success -> {
                        if (it.isToast){
                            successDialog(msg = it.msg)
                        }else{
                            showContent()
                        }
                    }
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
        successDialog.dismiss()
        errorDialog.dismiss()
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
        successDialog.dismiss()
        errorDialog.dismiss()
        if (!msg.isNullOrEmpty()) {
            loadingDialog.updateText(msg)
        }
        loadingDialog.show()
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
        successDialog.dismiss()
        errorDialog.dismiss()
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
        successDialog.dismiss()
        errorDialog.dismiss()
        loadingDialog.dismiss()
        _rootBinding.baseLoadingLayout.root.hide()
        _rootBinding.baseEmptyLayout.root.hide()
        _rootBinding.baseErrorLayout.root.showAnim()
        _rootBinding.baseContentLayout.hide()
    }

    /**
     * 显示错误弹窗
     */
    fun errorDialog(msg: String? = null) {
        successDialog.dismiss()
        loadingDialog.dismiss()
        if (!msg.isNullOrEmpty()) {
            errorDialog.updateDesc(msg)
        }
        errorDialog.show()
    }

    /**
     * 显示成功弹窗
     */
    fun successDialog(msg: String? = null) {
        loadingDialog.dismiss()
        errorDialog.dismiss()
        if (!msg.isNullOrEmpty()) {
            successDialog.updateDesc(msg)
        }
        successDialog.show()
    }

    override fun onError(message: String?,isToast: Boolean, isDialog: Boolean, code: Int?) {
        if (isDialog){
            if (isToast){
                showContent()
                // 弹出toast
                message?.toast()
            }else{
                // 显示错误弹窗
                errorDialog(message)
            }
        }else{
            if (isToast){
                showContent()
                // 弹出toast
                message?.toast()
            }else{
                // 显示错误布局
                showError(message)
            }
        }
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

    /**
     * 根布局的padding
     */
    open fun rootPadding(){
        ViewCompat.setOnApplyWindowInsetsListener(_rootBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

}