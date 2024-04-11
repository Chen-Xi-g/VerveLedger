package com.griffin.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.therouter.TheRouter

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

fun String?.router(bundle: (Bundle.() -> Unit)? = null){
    if (this.isNullOrBlank()) return
    val build = TheRouter.build(this)
    bundle?.let {
        Bundle().apply(bundle).let { b ->
            build.fillParams {
                it.putAll(b)
            }
        }
    }
    build.navigation()
}

fun String?.router(bundle: Bundle?){
    router {
        putAll(bundle)
    }
}

fun String?.routerForResult(activity: Activity, requestCode: Int, bundle: (Bundle.() -> Unit)? = null){
    if (this.isNullOrBlank()) return
    val build = TheRouter.build(this)
    bundle?.let {
        Bundle().apply(bundle).let { b ->
            build.fillParams {
                it.putAll(b)
            }
        }
    }
    build.navigation(activity, requestCode)
}