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
import com.griffin.core.data.model.BillListModel
import com.griffin.core.data.model.BillModel
import com.griffin.core.router.RoutePath
import com.griffin.core.rv.addData
import com.griffin.core.rv.linear
import com.griffin.core.rv.setData
import com.griffin.core.rv.setup
import com.griffin.core.utils.LogUtils
import com.griffin.core.utils.f2y
import com.griffin.core.utils.router
import com.griffin.core.utils.statusHeight
import com.griffin.feature.home.R
import com.griffin.feature.home.databinding.AdapterHomeBillBinding
import com.griffin.feature.home.databinding.AdapterHomeBillDetailBinding
import com.griffin.feature.home.databinding.FragmentHomeBinding
import com.therouter.TheRouter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : HiltBaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.vPadding.statusHeight()
        binding.vm = viewModel
        showContent()
        initAdapter()
    }

    override fun obtainData() {
        viewModel.getBillList()
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.billList.collectLatest {
                binding.rvDetail.setData(it)
                binding.refreshLayout.finishRefresh()
            }
        }
    }

    override fun onError(message: String?, isToast: Boolean, isDialog: Boolean, code: Int?) {
        super.onError(message, isToast, isDialog, code)
        binding.refreshLayout.finishRefresh(false)
    }

    private fun initAdapter(){
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getBillList()
        }
        binding.rvDetail.linear()
            .setup {
                setHasStableIds(true)
                // 添加多布局
                addType<BillModel>(R.layout.adapter_home_bill)
                addType<BillListModel>(R.layout.adapter_home_bill_detail)
                // 绑定数据
                onBind {
                    when(val type = getType()){
                        is BillModel ->{
                            // 父布局
                            getBinding<AdapterHomeBillBinding>()?.apply {
                                item = type
                            }
                        }
                        is BillListModel ->{
                            // 子布局
                            getBinding<AdapterHomeBillDetailBinding>()?.apply {
                                item = type
                            }
                        }
                    }
                }
                // 点击事件
                onItemClick {
                    when(val type = getType()){
                        is BillModel ->{
                            // 父布局点击事件
                            expandOrCollapse()
                        }
                        is BillListModel ->{
                            // 子布局点击事件
                            RoutePath.Add.BILL.router {
                                putInt("type", 2)
                                putLong("id", type.billId)
                            }
                        }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBillList()
    }

}