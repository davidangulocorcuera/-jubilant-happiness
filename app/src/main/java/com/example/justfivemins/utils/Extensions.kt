package com.example.justfivemins.utils

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build

import android.support.annotation.ColorRes

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView


class Extensions {
}

fun View.setVisible(show: Boolean, invisible: Boolean = false) {
    when {
        show -> this.visibility = View.VISIBLE
        invisible -> this.visibility = View.INVISIBLE
        else -> this.visibility = View.GONE
    }
}

fun View.isVisible(): Boolean {
    when {
        this.visibility == View.VISIBLE -> return true
        this.visibility == View.INVISIBLE -> return false
        this.visibility == View.GONE -> return false
    }
    return false
}

fun ImageView.tint(@ColorRes id: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
    this.setColorFilter(ContextCompat.getColor(context, id), mode)
}
fun Context.color(@ColorRes id: Int): Int {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> return getColor(id)
        else -> return resources.getColor(id)
    }
}






