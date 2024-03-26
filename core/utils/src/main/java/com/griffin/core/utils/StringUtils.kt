package com.griffin.core.utils

import java.math.BigDecimal

/**
 * 分价格转换为带有¥符号的价格
 */
fun Long?.price(): String{
    return f2y().addYuan()
}

/**
 * 添加¥符号
 */
fun String.addYuan() = Utils.context.getString(R.string.base_str_yuan, this)

/**
 * 分转元
 */
fun Long?.f2y(): String {
    if (this == null || this == 0L) {
        return "0.00"
    }
    return BigDecimal.valueOf(this.toLong()).divide(BigDecimal(100)).toString()
}