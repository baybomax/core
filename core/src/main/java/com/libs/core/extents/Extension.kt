package com.libs.core.extents

import android.content.Intent
import android.view.View
import com.google.gson.Gson
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

/**
 * Replace slashes ('/') in given path string with [File.separatorChar]
 *
 * @return path translated.
 */
fun String.slashes(): String {
    return replace('/', File.separatorChar)
}

/**
 * Return a object from the intent specified key
 * @param key the extra key
 * @param clazz the Class type of object
 */
fun <D> Intent.obj(key: String, clazz: Class<D>): D? =
    getStringExtra(key)?.let {
        try {
            Gson().fromJson(it, clazz)
        } catch (e: Exception) {
            null
        }
    }

/**
 * Try - Catch
 *
 * @param t the try block
 * @param c the catch block
 */
fun tryCatch(c: ((e: Exception) -> Unit)? = null,
             t: () -> Unit) {
    try {
        t()
    } catch (e: Exception) {
        c?.invoke(e)
    }
}
/**
 * Try - Catch
 */
fun <T> tryWith(handle: ()->T?) : T? = try {
    handle()
} catch (e: Exception) {
    null
}
