package com.griffin.core.utils

import android.app.UiModeManager
import android.content.Context

// 方法1：使用 UiModeManager
fun isDarkTheme(): Boolean {
    val uiModeManager = Utils.context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
    return uiModeManager?.nightMode == UiModeManager.MODE_NIGHT_YES
}