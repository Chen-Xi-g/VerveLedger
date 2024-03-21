package com.griffin.core.utils.mmkv

import android.view.View

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