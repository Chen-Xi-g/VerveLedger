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
import com.griffin.core.base.activity.BaseActivity
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.base.web.WebActivity
import com.griffin.core.utils.mmkv.gone
import com.griffin.core.utils.mmkv.start
import com.griffin.core.utils.mmkv.toast
import com.griffin.feature.login.R
import com.griffin.feature.login.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : HiltBaseActivity<ActivityLoginBinding>() {

    private var doubleBackToExitPressedOnce = false

    override fun initView(savedInstanceState: Bundle?) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // 修改v_padding的高度
            binding.vPadding.layoutParams.height = systemBars.top
            insets
        }
        // 透明状态栏
        rootBinding.baseTitleLayout.root.gone()
        showContent()
        initClick()
        initSpannable()
        initBackPress()
    }

    override val viewModel: LoginViewModel by viewModels()

    override fun obtainData() {
        refreshCode()
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.captchaImage.collectLatest {
                binding.ivCodeContent.setImageBitmap(it.bitmap)
            }
        }
    }

    private fun initClick(){
        binding.ivCodeContent.setOnClickListener {
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

    private fun refreshCode(){
        viewModel.getCaptchaImage()
    }

}