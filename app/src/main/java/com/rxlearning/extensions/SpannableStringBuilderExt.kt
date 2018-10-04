package com.rxlearning.extensions

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.FontRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

fun SpannableStringBuilder.setClickableSpan(context: Context, clickableText: String, text: String,
                                            @ColorRes clickableColor: Int, @FontRes fontRes: Int,
                                            @ColorRes clickableBackgroundColor: Int,
                                            isUnderline: Boolean = false,
                                            onClick: (view: View) -> Unit) {

    val startIndex = text.indexOf(clickableText)
    if (text.contains(clickableText)) {
        setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        onClick(view)
                    }

                    override fun updateDrawState(tp: TextPaint) {
                        super.updateDrawState(tp)
                        context.let {
                            with(tp) {
                                color = ContextCompat.getColor(it, clickableColor)
                                isUnderlineText = isUnderline
                                typeface = ResourcesCompat.getFont(it, fontRes)
                                bgColor = ContextCompat.getColor(it, clickableBackgroundColor)
                            }
                        }
                    }
                },
                startIndex,
                startIndex + clickableText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}