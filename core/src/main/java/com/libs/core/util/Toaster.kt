package com.libs.core.util

import android.content.Context
import android.widget.Toast

/**
 * To show toast message.
 */
object Toaster {

    /**
     * Show toast.
     * @param context The context.
     * @param text The char sequence.
     */
    fun showToast(context: Context, text: CharSequence) {
        Toast
            .makeText(
                context,
                text,
                Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * Show toast.
     * @param context The context.
     * @param resId The resource id.
     */
    fun showToast(context: Context, resId: Int) {
        Toast
            .makeText(
                context,
                resId,
                Toast.LENGTH_SHORT)
            .show()
    }

}