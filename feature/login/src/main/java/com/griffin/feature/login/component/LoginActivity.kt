package com.griffin.feature.login.component

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.base.web.WebActivity
import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.router.RoutePath
import com.griffin.core.utils.gone
import com.griffin.core.utils.start
import com.griffin.core.utils.statusHeight
import com.griffin.core.utils.toast
import com.griffin.feature.login.R
import com.griffin.feature.login.component.activate.ActivateActivity
import com.griffin.feature.login.component.forget.ForgetActivity
import com.griffin.feature.login.component.register.RegisterActivity
import com.griffin.feature.login.databinding.ActivityLoginBinding
import com.therouter.TheRouter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : HiltBaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    override val viewModel: LoginViewModel by viewModels()

    private var doubleBackToExitPressedOnce = false

    override fun initView(savedInstanceState: Bundle?) {
        binding.vPadding.statusHeight()
        rootBinding.baseTitleLayout.root.gone()
        showContent()
        initClick()
        initSpannable()
        initBackPress()
    }

    override fun obtainData() {
        refreshCode()
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                if (it is CaptchaImageModel) {
                    binding.ivCodeContent.setImageBitmap(it.bitmap)
                } else if (it is Boolean && it) {
                    TheRouter.build(RoutePath.Main.MAIN)
                        .navigation()
                    finish()
                }
            }
        }
    }

    private fun initClick() {
        binding.ivCodeContent.setOnClickListener {
            refreshCode()
        }
        binding.tvRegister.setOnClickListener {
            start(RegisterActivity::class.java, isFinish = false)
        }
        binding.tvForgetPassword.setOnClickListener {
            start(ForgetActivity::class.java, isFinish = false)
        }
        binding.tvActivateAccount.setOnClickListener {
            start(ActivateActivity::class.java, isFinish = false)
        }
        binding.tvLogin.setOnClickListener {
            login()
        }
        errorDialog.setOnDismissListener {
            refreshCode()
        }
    }

    /**
     * 初始化返回按钮
     */
    private fun initBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            if (doubleBackToExitPressedOnce) {
                // 不杀死应用，返回到系统桌面
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                doubleBackToExitPressedOnce = true
                "再按一次退出".toast()
                rootBinding.root.postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            }
        }
    }

    /**
     * 初始化隐私协议的Spannable
     */
    private fun initSpannable() {
        val spannable = SpannableString(getString(R.string.login_str_agreement))
        // 用户协议点击事件
        val userAgreement = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // 跳转到用户协议页面
                start(WebActivity::class.java, WebActivity.params(type = 1), false)
            }
        }
        // 隐私政策点击事件
        val privacyPolicy = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // 跳转到隐私协议页面
                start(WebActivity::class.java, WebActivity.params(type = 2), false)
            }
        }
        // 设置点击事件
        spannable.setSpan(userAgreement, 9, 15, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(privacyPolicy, 16, 22, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        // 设置点击区域的颜色
        spannable.setSpan(
            getColor(com.griffin.core.base.R.color.color_primary),
            9,
            15,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            getColor(com.griffin.core.base.R.color.color_primary),
            16,
            22,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvAgreement.text = spannable
        binding.tvAgreement.movementMethod = LinkMovementMethod.getInstance()
        binding.tvAgreement.highlightColor = Color.TRANSPARENT
    }

    /**
     * 重写此方法，删除父逻辑，不需要填充状态栏
     */
    override fun paddingWindow() {
    }

    private fun refreshCode() {
        viewModel.getCaptchaImage()
    }

    private fun login(){
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val code = binding.etCode.text.toString()
        viewModel.login(username, password, code)
    }

}