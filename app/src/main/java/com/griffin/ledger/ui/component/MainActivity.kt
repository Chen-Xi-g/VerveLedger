package com.griffin.ledger.ui.component

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.activity.BaseActivity
import com.griffin.ledger.R
import com.griffin.ledger.ui.adapter.MainViewPager2Adapter
import com.griffin.ledger.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<MainVM, ActivityMainBinding>() {

    private val viewPager2Adapter by lazy {
        MainViewPager2Adapter(supportFragmentManager, lifecycle)
    }

    override fun initView(savedInstanceState: Bundle?) {
        rootBinding.baseTitleLayout.root.visibility = View.GONE
        initViewPager2()
        initNavigationMenu()
        showContent()
    }

    override fun obtainData() {
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.currentPage.collectLatest { currentItem ->
                when(currentItem){
                    0 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.detail
                    }
                    1 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.statistics
                    }
                    2 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.assets
                    }
                    3 -> {
                        binding.bottomNavigationView.selectedItemId = R.id.mine
                    }
                }
            }
        }
    }

    /**
     * 初始化NavigationMenu
     */
    private fun initNavigationMenu(){
        binding.bottomAppBar.setOnApplyWindowInsetsListener(null)
        binding.bottomAppBar.setPadding(0,0,0,0)
        binding.bottomNavigationView.setOnApplyWindowInsetsListener(null)
        binding.bottomNavigationView.setPadding(0,0,0,0)
        navigationBarColor(com.griffin.core.base.R.color.color_surface)
        binding.bottomNavigationView.setOnItemSelectedListener {
            return@setOnItemSelectedListener when(it.itemId){
                R.id.detail -> {
                    binding.viewPager.currentItem = 0
                    true
                }
                R.id.statistics -> {
                    binding.viewPager.currentItem = 1
                    true
                }
                R.id.assets -> {
                    binding.viewPager.currentItem = 2
                    true
                }
                R.id.mine -> {
                    binding.viewPager.currentItem = 3
                    true
                }
                else -> false
            }
        }
    }

    /**
     * 初始化ViewPager2
     */
    private fun initViewPager2() {
        binding.viewPager.offscreenPageLimit = 4
        binding.viewPager.adapter = viewPager2Adapter
        binding.viewPager.registerOnPageChangeCallback(viewModel.onPageChangeCallback)
    }

    override fun onDestroy() {
        binding.viewPager.unregisterOnPageChangeCallback(viewModel.onPageChangeCallback)
        super.onDestroy()
    }

}