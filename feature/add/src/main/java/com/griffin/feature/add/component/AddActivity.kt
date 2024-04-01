package com.griffin.feature.add.component

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.data.dto.UserAccountDto
import com.griffin.core.data.model.MapLocationModel
import com.griffin.core.data.model.TypeChildModel
import com.griffin.core.router.RoutePath
import com.griffin.core.utils.DecimalDigitsInputFilter
import com.griffin.feature.add.R
import com.griffin.feature.add.databinding.ActivityAddBinding
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.interceptor.NavigationCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddActivity : HiltBaseActivity<ActivityAddBinding>(R.layout.activity_add) {

    companion object {
        /**
         * 选择账单类型
         */
        private const val REQUEST_BILL_TYPE = 100

        /**
         * 选择账户
         */
        private const val REQUEST_BILL_ACCOUNT = 101

        /**
         * 选择地址
         */
        private const val REQUEST_BILL_ADDRESS = 102

        /**
         * 新增
         */
        const val PARAMS_TYPE_ADD = 0

        /**
         * 修改
         */
        const val PARAMS_TYPE_UPDATE = 1

        /**
         * 查看
         */
        const val PARAMS_TYPE_DETAIL = 2

        /**
         * 获取页面所需参数
         *
         * @param type 0：新增 1：修改 2：查看
         */
        fun getParams(type: Int = PARAMS_TYPE_ADD): Bundle {
            return Bundle().apply {
                putInt("type", type)
            }
        }
    }

    override val viewModel: AddViewModel by viewModels()

    /**
     * 页面类型
     *
     * 0：新增 1：修改 2：查看
     */
    @Autowired
    @JvmField
    var type: Int = PARAMS_TYPE_ADD

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(typeTitle())
        binding.isEnable = this.type == PARAMS_TYPE_ADD || this.type == PARAMS_TYPE_UPDATE
        binding.vm = viewModel
        showContent()
        binding.etAmount.filters = arrayOf(DecimalDigitsInputFilter())
    }

    override fun obtainData() {
    }

    /**
     * 获取标题名称
     */
    private fun typeTitle() : String{
        return when(this.type){
            PARAMS_TYPE_ADD -> {
                getString(R.string.add_str_add_bill)
            }
            PARAMS_TYPE_UPDATE -> {
                getString(R.string.add_str_update_bill)
            }
            else -> {
                getString(R.string.add_str_detail_bill)
            }
        }
    }

    private fun loadData(){
        if (this.type == PARAMS_TYPE_DETAIL || this.type == PARAMS_TYPE_UPDATE){
            // 加载详情
        }
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.navigateTo.collectLatest {
                // 跳转Activity
                when (it) {
                    AddViewModel.START_BILL_TYPE -> {
                        // 跳转到账单类型选择页面
                        startBillType()
                    }

                    AddViewModel.START_BILL_ACCOUNT -> {
                        // 跳转到账单账户选择页面
                        startBillAccount()
                    }

                    AddViewModel.START_BILL_ADDRESS -> {
                        // 跳转到账单地址选择页面
                        startBillAddress()
                    }

                    AddViewModel.FINISH_OK -> {
                        // 关闭页面
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
        }
    }

    private fun startBillType() {
        TheRouter.build(RoutePath.Type.TYPE)
            .navigation(this, REQUEST_BILL_TYPE)
    }

    private fun startBillAccount() {
        TheRouter.build(RoutePath.Account.ACCOUNT)
            .navigation(this, REQUEST_BILL_ACCOUNT)
    }

    private fun startBillAddress() {
        TheRouter.build(RoutePath.Map.MAP)
            .navigation(this, REQUEST_BILL_ADDRESS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            REQUEST_BILL_TYPE -> {
                // 获取返回数据
                val typeData = data?.getSerializableExtra("data") as? TypeChildModel ?: return
                viewModel.updatePayType(typeData)
            }

            REQUEST_BILL_ACCOUNT -> {
                val account = data?.getSerializableExtra("data") as? UserAccountDto ?: return
                viewModel.updateAccount(account)
            }

            REQUEST_BILL_ADDRESS -> {
                val location = data?.getSerializableExtra("data") as? MapLocationModel ?: return
                viewModel.updateLocation(location)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}