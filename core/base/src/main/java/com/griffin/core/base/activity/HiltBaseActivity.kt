package com.griffin.core.base.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
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
import com.griffin.core.utils.isVisible
import com.griffin.core.utils.runMain
import com.griffin.core.utils.toast
import com.therouter.TheRouter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.jessyan.autosize.AutoSizeCompat

/**
 * Activity的主要逻辑实现
 *
 * @param DB DataBinding
 */
abstract class HiltBaseActivity<DB: ViewDataBinding>(@LayoutRes val layoutResId: Int) : AbstractActivity(){

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

    abstract val viewModel: BaseViewModel

    /**
     * 加载中弹窗
     */
    val loadingDialog by lazy {
        LoadingDialog(this)
    }

    /**
     * 错误弹窗
     */
    val errorDialog by lazy {
        ErrorDialog(this)
    }

    /**
     * 成功弹窗
     */
    val successDialog by lazy {
        SuccessDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightNavigationBars = false
        }
        TheRouter.inject(this)
        lockOrientation(true)
        // 设置默认布局
        _rootBinding = DataBindingUtil.setContentView(this, R.layout.base_root_layout)
        _rootBinding.lifecycleOwner = this
        initContent()
        initClick()
        _rootBinding.baseLoadingLayout.root.showAnim()
        initView(savedInstanceState)
        paddingWindow()
        obtainData()
        registerObserver()
    }

    private fun initContent() {
        _rootBinding.baseContentLayout.removeAllViews()
        _binding = DataBindingUtil.inflate(layoutInflater, layoutResId, _rootBinding.baseContentLayout, true)
        _binding.lifecycleOwner = this
    }

    private fun initClick(){
        _rootBinding.baseErrorLayout.root.setOnClickListener {
            if (_rootBinding.baseErrorLayout.root.isVisible()){
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
                        onError(it.msg, it.isToast, false, it.code)
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
        successDialog.dismiss()
        errorDialog.dismiss()
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
        if (!msg.isNullOrEmpty()){
            _rootBinding.baseLoadingLayout.baseLoadingText.text = msg
        }
        errorDialog.dismiss()
        successDialog.dismiss()
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
        if (!msg.isNullOrEmpty()){
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
        if (!msg.isNullOrEmpty()){
            _rootBinding.baseEmptyLayout.baseEmptyText.text = msg
        }
        loadingDialog.dismiss()
        successDialog.dismiss()
        errorDialog.dismiss()
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
        _rootBinding.baseErrorLayout.baseLoadingText.text = msg ?: ""
        loadingDialog.dismiss()
        successDialog.dismiss()
        errorDialog.dismiss()
        _rootBinding.baseLoadingLayout.root.hide()
        _rootBinding.baseEmptyLayout.root.hide()
        _rootBinding.baseErrorLayout.root.showAnim()
        _rootBinding.baseContentLayout.hide()
    }

    /**
     * 显示错误弹窗
     */
    fun errorDialog(msg: String? = null) {
        loadingDialog.dismiss()
        successDialog.dismiss()
        if (!msg.isNullOrEmpty()){
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
        if (!msg.isNullOrEmpty()){
            successDialog.updateDesc(msg)
        }
        successDialog.show()
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
        if (visibility == View.VISIBLE){
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
        if (visibility != View.VISIBLE){
            return
        }
        visibility = View.INVISIBLE
    }

    override fun onError(message: String?, isToast: Boolean, isDialog: Boolean, code: Int?) {
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
     * 为根布局设置padding
     */
    override fun paddingWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(_rootBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            // 设置标题栏顶部padding
            _rootBinding.baseTitleLayout.root.setPadding(0,systemBars.top,0,0)
            insets
        }
    }

    /**
     * 锁定屏幕方向
     *
     * @param isPortrait 是否锁定竖屏
     */
    fun lockOrientation(isPortrait: Boolean) {
        requestedOrientation = if (isPortrait){
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }else{
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    /**
     * 资源适配
     */
    override fun getResources(): Resources {
        runMain {
            AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());//如果没有自定义需求用这个方法
            AutoSizeCompat.autoConvertDensity(super.getResources(), 375f, true);//如果有自定义需求就用这个方法
        }
        return super.getResources()
    }

    /**
     * 点击空白处隐藏软键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) ev.let {
            if (isShouldHideInput(currentFocus, it)) {
                val im =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideInput(view: View?, ev: MotionEvent): Boolean {
        if (view is EditText) {
            val l: IntArray = intArrayOf(0, 0)
            view.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom: Int = top + view.getHeight()
            val right: Int = (left
                    + view.getWidth())
            return !(ev.x > left && ev.x < right
                    && ev.y > top && ev.y < bottom)
        }
        return false
    }

    /**
     * 设置状态栏颜色
     */
    fun statusBarColor(@ColorRes color: Int){
        window.statusBarColor = getColor(color)
    }

    /**
     * 设置导航栏颜色
     */
    fun navigationBarColor(@ColorRes color: Int){
        window.navigationBarColor = getColor(color)
    }

}