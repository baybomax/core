package com.libs.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Datetime utility.
 */
object DatetimeUtil {

    /**
     * The ISO time formatter.
     */
    @SuppressLint("SimpleDateFormat")
    val iso_formatter: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .apply {
                timeZone = TimeZone.getTimeZone("GMT")
            }

    /**
     * The UTC time formatter.
     */
    @SuppressLint("SimpleDateFormat")
    val simple_formatter: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
}

/**
 * Return empty string if exception.
 *
 * @param body
 * @see SimpleDateFormat
 */
fun DatetimeUtil.emptyIf(body: () -> String): String =
    try {
        body()
    } catch (e: Exception) {
        e.printStackTrace()

        ""
    }

/**
 * Return medium datetime string
 *
 * @param org The ISO time
 * @return The formatted time : 'May 2, 2019'
 */
fun DatetimeUtil.toMediumDateString(ctx: Context?,
                                    org: String ?,
                                    formatter: SimpleDateFormat): String = emptyIf {
    DateFormat
        .getMediumDateFormat(ctx)
        .format(
            formatter.parse(org)
        )
}

/**
 * Return datetime string
 *
 * @param org The ISO time
 * @return The formatted time : '2019-10-28(T)16:00:00(Z)'
 */
fun DatetimeUtil.fromMediumDateString(ctx: Context?,
                                      org: String?,
                                      formatter: SimpleDateFormat): String = emptyIf {
    formatter.format(
        DateFormat
            .getMediumDateFormat(ctx)
            .parse(org)
    )
}

/**
 * Return long datetime
 *
 * @param org The ISO time
 * @return The formatted time : 'Monday, January 3, 2000'
 */
fun DatetimeUtil.toLongDateString(ctx: Context?,
                                  org: String?,
                                  formatter: SimpleDateFormat): String = emptyIf {
    formatter.parse(org).let {
        "${DateFormat.getLongDateFormat(ctx).format(it)} ${DateFormat.getTimeFormat(ctx).format(it)}"
    }
}
