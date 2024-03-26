package com.griffin.feature.login.component.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.data.model.CaptchaImageModel
import com.griffin.core.utils.gone
import com.griffin.core.utils.statusHeight
import com.griffin.feature.login.R
import com.griffin.feature.login.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : HiltBaseActivity<ActivityRegisterBinding>(R.layout.activity_register) {

    override val viewModel: RegisterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        binding.vPadding.statusHeight()
        rootBinding.baseTitleLayout.root.gone()
        showContent()
        initCLick()
    }

    override fun obtainData() {
        refreshCode()
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                if (it is CaptchaImageModel){
                    binding.ivCodeContent.setImageBitmap(it.bitmap)
                }else if (it is Boolean && it){
                    finish()
                }
            }
        }
    }

    private fun initCLick(){
        binding.ibBack.setOnClickListener {
            finish()
        }
        binding.ivCodeContent.setOnClickListener {
            refreshCode()
        }
        binding.tvRegister.setOnClickListener {
            register()
        }
        errorDialog.setOnDismissListener {
            refreshCode()
        }
    }

    override fun paddingWindow() {
    }

    /**
     * 刷新验证码
     */
    private fun refreshCode(){
        viewModel.getCaptchaImage()
    }

    /**
     * 注册
     */
    private fun register() {
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val code = binding.etCode.text.toString()
        viewModel.register(
            code = code,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            username = username
        )
    }

}