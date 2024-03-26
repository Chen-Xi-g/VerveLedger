package com.griffin.ledger.ui.component.guide

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.griffin.core.utils.mmkv.BaseMV
import com.griffin.core.utils.start
import com.griffin.feature.login.component.LoginActivity
import com.griffin.ledger.R
import com.griffin.ledger.databinding.ActivityGuideBinding
import com.griffin.ledger.ui.adapter.GuideViewPager2Adapter

class GuideActivity : AppCompatActivity() {

    private val adapter by lazy {
        GuideViewPager2Adapter(supportFragmentManager, lifecycle)
    }

    private lateinit var binding: ActivityGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guide)
        initAdapter()
        binding.tvIndex.text = getString(R.string.app_format_index, "1", "2")
        binding.tvSkip.setOnClickListener {
            BaseMV.System.isFirst = false
            start(LoginActivity::class.java)
        }
        binding.tvEnterApp.setOnClickListener {
            BaseMV.System.isFirst = false
            start(LoginActivity::class.java)
        }
    }

    private fun initAdapter() {
        binding.vpGuide.adapter = adapter
        binding.vpGuide.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvIndex.text =
                    getString(R.string.app_format_index, (position + 1).toString(), "2")
                binding.tvEnterApp.visibility = if (position == 1) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })
    }


}