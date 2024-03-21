package com.griffin.ledger.ui.component

import androidx.viewpager2.widget.ViewPager2
import com.griffin.core.base.vm.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainVM : BaseViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    /**
     * ViewPager2页面切换监听
     */
    val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            _currentPage.value = position
        }
    }

}