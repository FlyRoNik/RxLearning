package com.rxlearning.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned

fun String.removeSpaces() = this.replace(" ", "")

fun String.removeSlash() = this.replace("/".toRegex(), "")

fun String.substringBefore(endIndex: Int) = this.substring(0, endIndex)

fun String.fromHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT, null, tagHandler)
} else {
    Html.fromHtml(this, null, tagHandler)
}

private val tagHandler = Html.TagHandler { opening, tag, output, _ ->
    when (tag) {
        "ul" -> if (!opening) output.append("\n")
        "li" -> if (opening) output.append("\n\t•")
    }
}

fun String.isOneSign() = length == 1