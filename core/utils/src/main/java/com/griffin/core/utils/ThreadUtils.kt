package com.griffin.core.utils

import android.os.Handler
import android.os.Looper

fun runMain(block: () -> Unit){
    if(Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        Handler(Looper.getMainLooper()).post {
            block()
        }
    }
}