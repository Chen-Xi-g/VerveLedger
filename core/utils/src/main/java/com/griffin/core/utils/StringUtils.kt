package com.griffin.core.utils

import java.math.BigDecimal

/**
 * 添加¥符号
 */
fun String.addFirstYuan() = Utils.context.getString(R.string.base_str_first_yuan, this)

/**
 * 添加¥符号
 */
fun String.addLastYuan() = Utils.context.getString(R.string.base_str_last_yuan, this)

/**
 * 分转元
 */
fun Long?.f2y(): String {
    if (this == null || this == 0L) {
        return "0"
    }
    return BigDecimal.valueOf(this.toLong()).divide(BigDecimal(100)).toString()
}

/**
 * 分转元
 */
fun Int?.f2y(): String {
    if (this == null || this == 0) {
        return "0"
    }
    return BigDecimal.valueOf(this.toLong()).divide(BigDecimal(100)).toString()
}

/**
 * 分转元
 */
fun Long?.f2yFirst(): String {
    return f2y().addFirstYuan()
}

/**
 * 分转元
 */
fun Int?.f2yFirst(): String {
    return f2y().addFirstYuan()
}

/**
 * 分转元
 */
fun Long?.f2yLast(): String {
    return f2y().addLastYuan()
}

/**
 * 分转元
 */
fun Int?.f2yLast(): String {
    return f2y().addLastYuan()
}

/**
 * 元转分
 */
fun String?.y2f(): Long {
    if (this.isNullOrBlank()){
        return 0
    }
    if (this.endsWith(".")){
        return BigDecimal(this.substring(0, this.length - 1)).multiply(BigDecimal(100)).toLong()
    }
    return BigDecimal(this).multiply(BigDecimal(100)).toLong()
}

/**
 * 卡号脱敏
 */
fun String?.desensitization(): String{
    if (isNullOrEmpty() || length <= 4){
        return "****"
    }
    // 判断字符串长度是否大于4，如果大于4则截取后四位，如果小于等于4则返回****
    return "**** " + substring(length - 4, length)
}