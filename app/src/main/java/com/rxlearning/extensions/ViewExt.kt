package com.rxlearning.extensions

import android.support.annotation.DrawableRes
import android.view.View
import android.view.View.*
import com.rxlearning.RxLearningApp

fun View.OnClickListener.setClickListeners(vararg views: View) {
    views.forEach { view -> view.setOnClickListener(this) }
}

fun View.OnLongClickListener.setLongClickListener(vararg views: View) {
    views.forEach { view -> view.setOnLongClickListener(this) }
}

fun View.OnLongClickListener.removeLongClickListener(vararg views: View) {
    views.forEach { view -> view.setOnLongClickListener(null) }
}

fun View.setDrawable(@DrawableRes drawableRes: Int) {
    background = RxLearningApp.instance.getDrawable(drawableRes)
}

fun View.hide(gone: Boolean = false) {
    visibility = if (gone) GONE else INVISIBLE
}

fun View.show() {
    visibility = VISIBLE
}