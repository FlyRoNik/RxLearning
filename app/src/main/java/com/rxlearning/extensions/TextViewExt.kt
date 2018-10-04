package com.rxlearning.extensions

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.rxlearning.RxLearningApp

fun TextView.setTextColorCompat(@ColorRes colorRes: Int) {
    setTextColor(ContextCompat.getColor(RxLearningApp.instance, colorRes))
}

fun TextView.getStringText() = this.text.toString()
