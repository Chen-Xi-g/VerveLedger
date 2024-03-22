package com.griffin.core.base.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.griffin.core.base.activity.HiltBaseActivity
import com.griffin.core.base.databinding.BaseActivityWebBinding
import com.therouter.router.Autowired
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WebActivity : HiltBaseActivity<BaseActivityWebBinding>() {

    companion object{

        /**
         * 获取参数
         *
         * @param url 链接
         * @param type 类型 0：链接 1：用户协议 2：隐私政策
         */
        fun params(url: String? = null, type: Int = 0): Bundle{
            return Bundle().apply {
                putString("url", url)
                putInt("type", type)
            }
        }
    }

    @Autowired
    @JvmField
    var url: String? = null

    @Autowired
    @JvmField
    var type: Int = 0

    override val viewModel: WebViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        initWeb()
    }

    override fun obtainData() {
        loadData()
    }

    override fun registerObserver() {
        super.registerObserver()
        lifecycleScope.launch {
            viewModel.agreementContent.collectLatest {
                loadRichText(it)
            }
        }
    }

    private fun loadData(){
        if (type != 0){
            setTitleName(if (type == 1) "用户协议" else "隐私政策")
            viewModel.getAgreementContent(type)
        }else{
            setTitleName("网络浏览器")
            showContent()
            url?.let {
                loadUrl(it)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWeb(){
        // 启用JavaScript
        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
    }

    private fun loadRichText(richText: String) {
        // 添加最小的HTML结构
        val htmlData = "<html><head><meta charset='utf-8'></head><body>$richText</body></html>"
        // 使用WebView加载富文本内容
        binding.webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
    }

    private fun loadUrl(url: String){
        binding.webView.loadUrl(url)
    }

    override fun onRetry() {
        super.onRetry()
        loadData()
    }

}