package com.griffin.feature.add.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.data.dto.UserAccountDto
import com.griffin.core.data.model.MapLocationModel
import com.griffin.core.data.model.TypeChildModel
import com.griffin.core.router.RoutePath
import com.griffin.core.utils.DecimalDigitsInputFilter
import com.griffin.core.utils.TimeUtils
import com.griffin.core.utils.TimeUtils.strToDate
import com.griffin.core.utils.router
import com.griffin.core.utils.routerForResult
import com.griffin.feature.add.R
import com.griffin.feature.add.databinding.ActivityAddBinding
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route
import com.therouter.router.interceptor.NavigationCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
@Route(path = RoutePath.Add.BILL)
class AddActivity : HiltBaseActivity<ActivityAddBinding>(R.layout.activity_add) {

    override val viewModel: AddViewModel by viewModels()

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
         * @param id 账单ID
         */
        fun getParams(type: Int = PARAMS_TYPE_ADD, id: Long? = null): Bundle {
            return Bundle().apply {
                putInt("type", type)
                if (id != null) {
                    putLong("id", id)
                }
            }
        }
    }

    /**
     * 页面类型
     *
     * 0：新增 1：修改 2：查看
     */
    @Autowired
    @JvmField
    var type: Int = PARAMS_TYPE_ADD

    /**
     * 账单ID
     */
    @Autowired
    @JvmField
    var id: Long? = null

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(typeTitle())
        navigationBarColor(com.griffin.core.base.R.color.color_surface)
        binding.isEnable = this.type == PARAMS_TYPE_ADD || this.type == PARAMS_TYPE_UPDATE
        binding.vm = viewModel
        viewModel.type = type
        showContent()
        binding.etAmount.filters = arrayOf(DecimalDigitsInputFilter())
        binding.tvSubmit.text = when (this.type) {
            PARAMS_TYPE_ADD -> {
                getString(R.string.add_str_save_bill)
            }

            PARAMS_TYPE_UPDATE -> {
                getString(R.string.add_str_save_bill)
            }

            else -> {
                getString(R.string.add_str_update_bill)
            }
        }
    }

    override fun obtainData() {
        if (this.type == PARAMS_TYPE_UPDATE) {
            viewModel.getBillDetail(id)
        }
    }

    /**
     * 获取标题名称
     */
    private fun typeTitle(): String {
        return when (this.type) {
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

    private fun loadData() {
        if (this.type == PARAMS_TYPE_DETAIL) {
            // 加载详情
            viewModel.getBillDetail(id)
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

                    AddViewModel.START_BILL_DATE -> {
                        startBillDate()
                    }

                    AddViewModel.START_BILL_ACCOUNT -> {
                        // 跳转到账单账户选择页面
                        startBillAccount()
                    }

                    AddViewModel.START_BILL_ADDRESS -> {
                        // 跳转到账单地址选择页面
                        startBillAddress()
                    }

                    AddViewModel.START_BILL_UPDATE -> {
                        // 跳转到修改页面
                        RoutePath.Add.BILL.router(Companion.getParams(PARAMS_TYPE_UPDATE, id))
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

    /**
     * 选择账单类型
     */
    private fun startBillType() {
        RoutePath.Type.TYPE.routerForResult(this, REQUEST_BILL_TYPE)
    }

    /**
     * 选择日期
     */
    private fun startBillDate() {
        // 获取当前时间
        val calendar = Calendar.getInstance()
        calendar.time =
            viewModel.uiState.value.createTime.strToDate(TimeUtils.FORMAT_YYYY_MM_DD_HH_MM)

        // 显示日期选择器对话框
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // 用户选择日期后，显示时间选择器对话框
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _: TimePicker, hourOfDay: Int, minute: Int ->
                        // 用户选择时间后，将日期和时间合并
                        val selectedDateTime = String.format(
                            Locale.getDefault(),
                            "%04d-%02d-%02d %02d:%02d",
                            year,
                            monthOfYear + 1,
                            dayOfMonth,
                            hourOfDay,
                            minute
                        )
                        viewModel.updateDate(selectedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    /**
     * 选择账户
     */
    private fun startBillAccount() {
        RoutePath.Account.ACCOUNT.routerForResult(this, REQUEST_BILL_ACCOUNT)
    }

    /**
     * 选择地点
     */
    private fun startBillAddress() {
        if (this.type == PARAMS_TYPE_DETAIL) {
            // 地址为空，说明没有位置
            if (viewModel.uiState.value.address.isBlank()) {
                return
            }
            RoutePath.Map.MAP_DETAIL.router {
                putInt("type", 1)
                putLong("id", id ?: 0)
            }
        } else {
            RoutePath.Map.MAP.routerForResult(this, REQUEST_BILL_ADDRESS)
        }
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