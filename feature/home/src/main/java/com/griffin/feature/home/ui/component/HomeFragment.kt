package com.griffin.feature.home.ui.component

import android.os.Bundle
import android.view.View
import com.griffin.core.base.fragment.BaseFragment
import com.griffin.feature.home.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override fun initView(view: View, savedInstanceState: Bundle?) {
        showContent()
    }

    override fun obtainData() {
    }

}