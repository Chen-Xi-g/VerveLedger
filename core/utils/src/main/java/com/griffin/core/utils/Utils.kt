package com.griffin.core.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV

@SuppressLint("StaticFieldLeak")
object Utils {
    internal lateinit var context: Context

    /**
     * 初始化 MMKV
     */
    fun initialize(application: Application){
        context = application
        MMKV.initialize(application)
    }
}