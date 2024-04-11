package com.griffin.core.base

import android.app.Application
import android.content.res.Configuration
import com.griffin.core.network.BuildConfig
import com.griffin.core.utils.Utils
import me.jessyan.autosize.AutoSizeConfig
import java.util.Locale

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AutoSizeConfig.getInstance().setExcludeFontScale(true)
        Utils.initialize(this, BuildConfig.DEBUG)
        setLocale(Locale.SIMPLIFIED_CHINESE)
    }

    /**
     * 设置全局语言环境
     *
     * @param locale 语言、国家/地区和可选的变体信息
     */
    fun setLocale(locale: Locale){
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
    }
}