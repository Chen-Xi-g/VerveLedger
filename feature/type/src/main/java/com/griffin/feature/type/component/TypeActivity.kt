package com.griffin.feature.type.component

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.router.RoutePath
import com.griffin.core.utils.toast
import com.griffin.feature.type.R
import com.griffin.feature.type.adapter.TypeVpAdapter
import com.griffin.feature.type.databinding.ActivityTypeBinding
import com.therouter.router.Route
import dagger.hilt.android.AndroidEntryPoint

/**
 * 选择类型页面
 */
@AndroidEntryPoint
@Route(path = RoutePath.Type.TYPE)
class TypeActivity : HiltBaseActivity<ActivityTypeBinding>(R.layout.activity_type) {

    override val viewModel: TypeViewModel by viewModels()

    private val adapter by lazy {
        TypeVpAdapter(supportFragmentManager, lifecycle)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName("账单类型")
        initAdapter()
        showContent()
    }

    override fun obtainData() {
    }

    override fun registerObserver() {
        super.registerObserver()
    }

    private fun initAdapter(){
        binding.tvConfirm.setOnClickListener {
            val currentType = if (binding.rbExpenses.isChecked){
                // 支出
                adapter.getFragment(0).currentType()
            }else if (binding.rbIncome.isChecked) {
                // 收入
                adapter.getFragment(1).currentType()
            }else{
                null
            }
            if (currentType == null){
                "请选择类型".toast()
                return@setOnClickListener
            }
            val intent = Intent()
            intent.putExtra("data", currentType)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.vpType.offscreenPageLimit = 2
        binding.vpType.adapter = adapter
        binding.vpType.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.rbGroup.check(if (position == 0) R.id.rb_expenses else R.id.rb_income)
            }
        })

        binding.rbGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.vpType.currentItem = if (checkedId == R.id.rb_expenses) 0 else 1
        }
    }

}