package com.libs.core.extents

import android.app.Dialog
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.widget.TextView
import com.libs.core.util.Toaster

/**
 * Convenient function to setOnClickedListeners of views
 *
 * @param ids The ids array
 * @param callback
 */
fun <V: View> View.setOnClickedListeners(vararg ids: Int, callback: (View) -> Unit) {
    ids.forEach {
        findViewById<V>(it)
            ?.setOnClickListener { v ->
                callback(v)
            }
    }
}

/**
 * Convenient function to setOnClickedListeners of dialog
 *
 * @param ids The ids array
 * @param callback
 */
fun <V: View> Dialog.setOnClickedListeners(vararg ids: Int, callback: (View) -> Unit) {
    ids.forEach {
        findViewById<V>(it)
            ?.setOnClickListener { v ->
                callback(v)
            }
    }
}

/**
 * Convenient function to set text if given text is not null or empty
 *
 * @param text The charSequence text
 */
fun TextView.textOrEmpty(text: CharSequence?) =
    run {
        this.text = text ?: ""
    }

/**
 * Convenient function to show msg when text view content is empty
 *
 * @param resId The msg resources id
 * @return Null if text is empty, false text content
 */
fun TextView.emptyWithMsg(resId: Int): String? =
    text.toString().trim().let {
        if (it.isEmpty()) {
            Toaster.showToast(context, resId)
            null
        } else {
            it
        }
}

/**
 * Convenient function to set text color
 * @param resId The resource id of color
 */
fun TextView.textColor(resId: Int) =
    setTextColor(
        resources.color(resId, context.theme)
    )

/**
 *
 */
@Suppress("DEPRECATION")
fun Resources.color(resId: Int, theme: Resources.Theme): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        getColor(resId, theme)
    else
        getColor(resId)

