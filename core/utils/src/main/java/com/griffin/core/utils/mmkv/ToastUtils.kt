package com.griffin.core.utils.mmkv

import android.widget.Toast

object ToastUtils {

    private var toast: Toast? = null

    fun showToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
            cancelToast()
            runMain {
                toast = Toast.makeText(Utils.context, message, duration)
                toast?.show()
            }
    }

    private fun cancelToast() {
        toast?.cancel()
    }
}

/**
 * 显示Toast
 */
fun CharSequence.toast() {
    if (this.length > 8){
        toastShort()
    }else{
        toastLong()
    }
}

/**
 * 显示Toast, 默认短时间
 */
fun CharSequence.toastShort() {
    ToastUtils.showToast(this.toString(), Toast.LENGTH_SHORT)
}

/**
 * 显示Toast, 默认长时间
 */
fun CharSequence.toastLong() {
    ToastUtils.showToast(this.toString(), Toast.LENGTH_LONG)
}