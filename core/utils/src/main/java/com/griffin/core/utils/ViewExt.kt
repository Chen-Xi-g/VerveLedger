package com.griffin.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
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

/**
 * 布局转换为图片
 */
fun Context.layoutToBitmap(layoutResId: Int, initView: (View) -> Unit = {}): Bitmap {
    val view = LayoutInflater.from(this).inflate(layoutResId, null, false)
    initView(view)
    val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    view.measure(widthMeasureSpec, heightMeasureSpec)

    view.layout(0, 0, view.measuredWidth, view.measuredHeight)

    val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)

    view.draw(canvas)

    return bitmap
}