package com.griffin.feature.account.component.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.data.dto.UserAccountDto
import com.griffin.core.data.mappers.toAccountDto
import com.griffin.core.data.model.AccountListModel
import com.griffin.core.data.model.AccountModel
import com.griffin.core.router.RoutePath
import com.griffin.core.rv.linear
import com.griffin.core.rv.setData
import com.griffin.core.rv.setup
import com.griffin.feature.account.R
import com.griffin.feature.account.databinding.ActivityAccountBinding
import com.griffin.feature.account.databinding.AdapterAccountBinding
import com.griffin.feature.account.databinding.AdapterAccountChildBinding
import com.therouter.router.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 描述：
 */
@AndroidEntryPoint
@Route(path = RoutePath.Account.ACCOUNT)
class AccountActivity : HiltBaseActivity<ActivityAccountBinding>(R.layout.activity_account) {

    /**
     * 初始化ViewModel
     */
    override val viewModel: AccountViewModel by viewModels()

    /**
     * 初始化View
     */
    override fun initView(savedInstanceState: Bundle?) {
        setTitleName("我的账户")
        initAdapter()
    }

    /**
     * 获取数据
     */
    override fun obtainData() {
        loadData()
    }

    /**
     * 注册观察者
     */
    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.list.collectLatest {
                binding.recyclerView.setData(it)
                binding.refreshLayout.finishRefresh()
            }
        }
    }

    private fun initAdapter(){
        binding.refreshLayout.setOnRefreshListener {
            loadData()
        }
        binding.recyclerView.linear()
            .setup {
                addType<AccountModel>(R.layout.adapter_account)
                addType<AccountListModel>(R.layout.adapter_account_child)
                onBind {
                    when(val type = getType()){
                        is AccountModel -> {
                            getBinding<AdapterAccountBinding>()?.item = type
                        }
                        is AccountListModel -> {
                            getBinding<AdapterAccountChildBinding>()?.item = type
                        }
                    }
                }

                onItemClick {
                    when(val type = getType()){
                        is AccountListModel -> {
                            val intent = Intent()
                            intent.putExtra("data", type.toAccountDto())
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }
                }
            }
    }

    private fun loadData(){
        viewModel.accountList()
    }

}