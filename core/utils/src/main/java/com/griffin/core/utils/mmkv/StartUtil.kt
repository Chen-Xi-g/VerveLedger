package com.griffin.core.utils.mmkv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

fun Activity.intent(cls: Class<*>, block: Intent.() -> Unit = {}): Intent {
    return Intent(this, cls).apply(block)
}

fun Fragment.intent(cls: Class<*>, block: Intent.() -> Unit = {}): Intent {
    return Intent(requireContext(), cls).apply(block)
}

fun Context.intent(cls: Class<*>, block: Intent.() -> Unit = {}): Intent {
    return Intent(this, cls).apply(block)
}

fun Activity.start(clazz: Class<*>, bundle: Bundle? = null, isFinish: Boolean = true) {
    startActivity(intent(clazz){
        bundle?.let {
            putExtras(it)
        }
    })
    if (isFinish) {
        finish()
    }
}

fun Fragment.start(clazz: Class<*>, bundle: Bundle? = null, isFinish: Boolean = true) {
    startActivity(intent(clazz){
        bundle?.let {
            putExtras(it)
        }
    })
    if (isFinish) {
        requireActivity().finish()
    }
}

fun Context.start(clazz: Class<*>, block: Intent.() -> Unit = {}) {
    startActivity(intent(clazz, block))
}