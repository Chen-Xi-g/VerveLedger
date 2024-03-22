package com.griffin.feature.login.component.register

import android.os.Bundle
import androidx.activity.viewModels
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.feature.login.databinding.ActivityRegisterBinding

class RegisterActivity : HiltBaseActivity<ActivityRegisterBinding>() {

    override val viewModel: RegisterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun obtainData() {
    }

}