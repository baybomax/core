package com.libs.core.util

import android.app.Activity
import android.content.Context
import android.view.View
import java.io.File

fun <A: Any, B: Any, R> let2(a :A?, b: B?, handler: (A, B)->R): R? =
    if (null != a && null != b) {
        handler(a, b)
    } else {
        null
    }

fun <A: Any, B: Any, C: Any, R> let3(a :A?, b: B?, c: C?, handler: (A, B, C)->R): R? =
    if (null != a && null != b && null != c) {
        handler(a, b, c)
    } else {
        null
    }

val Boolean.visibleOrGone: Int
    get() = if (this) {
        View.VISIBLE
    } else {
        View.GONE
    }

val Boolean.visibleOrInvisible: Int
    get() = if (this) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }

// =======
// Context
// =======

/**
 * Convenient function to run on specify context thread.
 *
 * @param callback
 */
fun Context.runOnContextThread(callback: (()->Unit)?) = callback?.let {
    when (this) {
        is Activity -> runOnUiThread(it)
        else -> it.invoke()
    }
}

fun Context.showToast(resId: Int         ) = Toaster.showToast(this, resId)
fun Context.showToast(text : CharSequence) = Toaster.showToast(this, text )

// ======
// String
// ======

/**
 * Replace slashes ('/') in given path string with [File.separatorChar]
 *
 * @return path translated.
 */
fun String.slashes(): String {
    return replace('/', File.separatorChar)
}

