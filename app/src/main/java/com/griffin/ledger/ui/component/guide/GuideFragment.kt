package com.griffin.ledger.ui.component.guide

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.griffin.ledger.R
import com.therouter.TheRouter
import com.therouter.router.Autowired

class GuideFragment : Fragment(R.layout.fragment_guide) {

    companion object{

        /**
         * 创建实例
         *
         * @param index 索引
         */
        fun newInstance(index: Int): GuideFragment {
            val fragment = GuideFragment()
            fragment.arguments = Bundle().apply {
                putInt("index", index)
            }
            return fragment
        }
    }

    @Autowired
    @JvmField
    var index: Int = 0

    private lateinit var ivGuideBg: AppCompatImageView
    private lateinit var tvGuideTitle: AppCompatTextView
    private lateinit var tvGuideDesc: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheRouter.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivGuideBg = view.findViewById(R.id.iv_guide_bg)
        tvGuideTitle = view.findViewById(R.id.tv_guide_title)
        tvGuideDesc = view.findViewById(R.id.tv_guide_desc)
        when(index){
            0 -> {
                ivGuideBg.setImageResource(R.drawable.animated_guide_1)
                tvGuideTitle.text = "【高效快捷】"
                tvGuideDesc.text = "记录生活，掌握财务"
            }
            1 -> {
                ivGuideBg.setImageResource(R.drawable.animated_guide_2)
                tvGuideTitle.text = "【数据统计】"
                tvGuideDesc.text = "一目了然，财务分析"
            }
        }
    }

}