package com.rxlearning.extensions

import android.text.method.PasswordTransformationMethod
import android.widget.EditText

fun EditText.switchPassword(action: (isVisible: Boolean) -> Unit) {
    transformationMethod = if (transformationMethod != null) {
        action(true)
        null
    } else {
        action(false)
        PasswordTransformationMethod()
    }
    setSelection(text.length)
}

fun EditText.clear() = setText("")

fun EditText.setSelectionEnd() = setSelection(length())
