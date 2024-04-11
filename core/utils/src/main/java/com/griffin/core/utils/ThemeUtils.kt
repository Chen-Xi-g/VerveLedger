package com.griffin.core.utils

import android.app.UiModeManager
import android.content.Context

/**
 * 当前主题是否为深色
 *
 * @return true：深色，false：亮色
 */
fun isDarkTheme(): Boolean {
    val uiModeManager = Utils.context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
    return uiModeManager?.nightMode == UiModeManager.MODE_NIGHT_YES
}