package com.griffin.feature.login.component.activate

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.utils.gone
import com.griffin.core.utils.statusHeight
import com.griffin.feature.login.R
import com.griffin.feature.login.databinding.ActivityActivateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 描述：
 */
@AndroidEntryPoint
class ActivateActivity : HiltBaseActivity<ActivityActivateBinding>(R.layout.activity_activate) {

    /**
     * 初始化ViewModel
     */
    override val viewModel: ActivateViewModel by viewModels()

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?) {
        binding.vm = viewModel
        binding.vPadding.statusHeight()
        rootBinding.baseTitleLayout.root.gone()
        showContent()
        initClick()
    }

    /**
     * 获取数据
     */
    override fun obtainData() {
        refreshCode()
    }

    /**
     * 注册观察者
     */
    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.captchaImage.collect {
                binding.ivCodeContent.setImageBitmap(it.bitmap)
            }
        }
    }

    private fun initClick(){
        binding.ibBack.setOnClickListener {
            finish()
        }
        successDialog.setOnDismissListener {
            finish()
        }
        errorDialog.setOnDismissListener {
            refreshCode()
        }
    }

    /**
     * 重写此方法，删除父逻辑，不需要填充状态栏
     */
    override fun paddingWindow() {
    }

    private fun refreshCode() {
        viewModel.refreshCode()
    }

}