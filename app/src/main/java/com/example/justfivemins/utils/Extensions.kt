package com.example.justfivemins.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.TextInputLayout

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import com.example.justfivemins.R


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

fun Context.drawable(@DrawableRes id: Int?): Drawable? {
    id?.let {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> return resources.getDrawable(id, null)
            else -> return resources.getDrawable(id)
        }
    }
    return null
}

fun TextInputLayout.showError(hasError: Boolean) {
    this.isErrorEnabled = hasError
    if (hasError) {
        this.setHintTextAppearance(R.style.HintError)
        this.setBackgroundColor(context!!.color(R.color.editError))
    } else {
        this.setHintTextAppearance(R.style.HintNormal)
        this.background = context!!.drawable(R.drawable.edit_selector)
    }
    val padding = resources.getDimension(R.dimen.space_small).toInt()
    this.setPadding(0, padding, 0, padding)
}






