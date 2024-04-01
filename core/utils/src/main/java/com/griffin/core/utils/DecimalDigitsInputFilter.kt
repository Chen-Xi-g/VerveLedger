package com.griffin.core.utils

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils


class DecimalDigitsInputFilter(
    private var decimalPlaces: Int = 2
) : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val sourceContent = source.toString()
        val lastInputContent = dest.toString()

        //验证删除等按键
        if (TextUtils.isEmpty(sourceContent)) {
            return ""
        }
        //以小数点"."开头，默认为设置为“0.”开头
        if (sourceContent == "." && lastInputContent.length == 0) {
            return "0."
        }
        //输入“0”，默认设置为以"0."开头
        if (sourceContent == "0" && lastInputContent.length == 0) {
            return "0."
        }
        //小数点后保留两位
        if (lastInputContent.contains(".")) {
            val index = lastInputContent.indexOf(".")
            if (dend - index >= decimalPlaces + 1) {
                return ""
            }
        }
        return null
    }
}