package com.libs.core.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

/**
 * Utility class to given element info related screen.
 */
object Screen {

    /**
     * Utility function to get the screen width of the device.
     *
     * @param activity The activity
     * @return Screen width.
     */
    fun screenWidth(activity: Activity): Int = run {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        metrics.widthPixels
    }

    /**
     * Utility function to get the screen width of the device.
     *
     * @param activity The activity
     * @return Screen width.
     */
    fun screenHeight(activity: Activity) = run {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        metrics.heightPixels
    }

    /**
     * Convert SP to Pixels.
     *
     * @param context The context
     * @param sp size value in SPs.
     * @return size value in pixels
     */
    fun sp2Px(context: Context, sp: Float): Float =
        context.resources.displayMetrics.scaledDensity * sp

    /**
     * Convert from DIP (Device Independent Pixel) to Pixels
     *
     * @param context The context
     * @param dp size value in dip
     * @return size value in pixels.
     */
    fun dp2Px(context: Context, dp: Float): Float =
        (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT) * dp

}