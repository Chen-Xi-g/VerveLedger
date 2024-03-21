package com.griffin.feature.login.component

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.griffin.core.base.activity.BaseActivity
import com.griffin.core.utils.mmkv.gone
import com.griffin.feature.login.R
import com.griffin.feature.login.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
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
    }

    override fun obtainData() {
    }

    /**
     * 重写此方法，删除父逻辑，不需要填充状态栏
     */
    override fun paddingWindow() {
    }
}