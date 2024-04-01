package com.griffin.core.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


fun View.gone(){
    if (visibility != View.GONE){
        visibility = View.GONE
    }
}

fun View.visible(){
    if (visibility != View.VISIBLE){
        visibility = View.VISIBLE
    }
}

fun View.invisible(){
    if (visibility != View.INVISIBLE){
        visibility = View.INVISIBLE
    }
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

/**
 * 设置View高度为状态栏高度
 */
fun View.statusHeight(){
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        // 修改v_padding的高度
        val layoutParams = this.layoutParams
        layoutParams.height = systemBars.top
        this.layoutParams = layoutParams
        insets
    }
}