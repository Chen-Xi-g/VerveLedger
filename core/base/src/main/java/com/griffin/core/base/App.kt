package com.griffin.core.base

import android.app.Application
import com.griffin.core.utils.mmkv.Utils
import me.jessyan.autosize.AutoSizeConfig

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AutoSizeConfig.getInstance().setExcludeFontScale(true)
        Utils.initialize(this)
    }
}