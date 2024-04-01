package com.griffin.feature.type.component

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.fragment.HiltBaseFragment
import com.griffin.core.base.vm.BaseViewModel
import com.griffin.core.data.dto.UserPayTypeDto
import com.griffin.core.data.model.TypeChildModel
import com.griffin.core.data.model.TypeModel
import com.griffin.core.rv.data
import com.griffin.core.rv.linear
import com.griffin.core.rv.model.SelectSealed
import com.griffin.core.rv.reuseAdapter
import com.griffin.core.rv.setData
import com.griffin.core.rv.setup
import com.griffin.feature.type.R
import com.griffin.feature.type.databinding.AdapterTypeBinding
import com.griffin.feature.type.databinding.AdapterTypeChildBinding
import com.griffin.feature.type.databinding.FragmentTypeBinding
import com.therouter.router.Autowired
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TypeFragment : HiltBaseFragment<FragmentTypeBinding>(R.layout.fragment_type) {

    companion object {

        /**
         * 创建实例
         *
         * @param typeTag 类型选择，0：支出，1：收入
         */
        fun newInstance(typeTag: Int): TypeFragment {
            return TypeFragment().apply {
                this.typeTag = typeTag
            }
        }
    }

    /**
     * 类型选择，0：支出，1：收入
     */
    @Autowired
    @JvmField
    var typeTag: Int = 0

    override val viewModel: TypeViewModel by viewModels()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }

    override fun obtainData() {
        loadData()
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.typeList.collectLatest {
                binding.refreshLayout.finishRefresh()
                binding.recyclerView.setData(it)
                binding.recyclerView.reuseAdapter.removeFooterAll()
                binding.recyclerView.reuseAdapter.addFooter("- 暂无更多 -")
            }
        }
    }

    override fun onError(message: String?, isToast: Boolean, isDialog: Boolean, code: Int?) {
        super.onError(message, isToast, isDialog, code)
        binding.refreshLayout.finishRefresh(false)
    }

    private fun initAdapter() {
        binding.refreshLayout.setOnRefreshListener {
            // 刷新
            loadData()
        }
        binding.recyclerView.linear()
            .setup {
                selectModel = SelectSealed.Single
                setHasStableIds(true)
                addType<TypeModel>(R.layout.adapter_type)
                addType<TypeChildModel>(R.layout.adapter_type_child)
                addType<String>(com.griffin.core.base.R.layout.base_padding_footer)
                onBind {
                    when (val type = getType()) {
                        is TypeModel -> {
                            // 父类型
                            getBinding<AdapterTypeBinding>()?.item = type
                        }

                        is TypeChildModel -> {
                            // 子类型
                            getBinding<AdapterTypeChildBinding>()?.item = type
                        }
                    }
                }
                onItemClick {
                    val type = getType()
                    when (type) {
                        is TypeModel -> {
                            // 父类型
                            checkedSwitch(adapterPosition)
                            expandOrCollapse()
                        }

                        is TypeChildModel -> {
                            // 子类型
                            checkedSwitch(adapterPosition)
                        }
                    }
                }
            }
    }

    private fun loadData() {
        viewModel.load(typeTag)
    }

    /**
     * 当前选中的数据
     */
    fun currentType(): TypeChildModel? {
        val item = binding.recyclerView.data.firstOrNull {
            when (it) {
                is TypeModel -> {
                    it.isSelected
                }

                is TypeChildModel -> {
                    it.isSelected
                }

                else -> {
                    false
                }
            }
        }
        return when (item) {
            is TypeModel -> {
                TypeChildModel(
                    parentId = item.parentId,
                    typeId = item.typeId,
                    typeName = item.typeName,
                    typeTag = item.typeTag
                )
            }

            is TypeChildModel -> {
                item
            }

            else -> {
                null
            }
        }
    }

}