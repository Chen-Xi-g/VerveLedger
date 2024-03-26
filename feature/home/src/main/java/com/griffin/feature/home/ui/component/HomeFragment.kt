package com.griffin.feature.home.ui.component

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.fragment.BaseFragment
import com.griffin.core.base.fragment.HiltBaseFragment
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.rv.addData
import com.griffin.core.rv.linear
import com.griffin.core.rv.setData
import com.griffin.core.rv.setup
import com.griffin.core.utils.f2y
import com.griffin.core.utils.statusHeight
import com.griffin.feature.home.R
import com.griffin.feature.home.databinding.AdapterHomeBillBinding
import com.griffin.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

class HomeFragment : HiltBaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.vPadding.statusHeight()
        showContent()
        initAdapter()
    }

    override fun obtainData() {
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
        }
    }

    private fun initAdapter(){
        binding.rvDetail.linear()
            .setup {
                addType<String>(R.layout.adapter_home_bill)
                onBind {
                    val binding = getBinding<AdapterHomeBillBinding>()
                    binding?.tvDate?.text = "2021-09-01"
                }
            }
        binding.rvDetail.setData(
            listOf("1","2","3")
        )
    }

}