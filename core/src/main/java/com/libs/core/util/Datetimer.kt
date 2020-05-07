package com.libs.core.util

import android.content.Context
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 */

object Datetimer {

    private fun <O> ifNull(body: () -> O?): O? {
        return try {
            body()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * parse
     */
    fun parse(formatter: SimpleDateFormat, org: String?): Date? {
        return ifNull { formatter.parse(org) }
    }

    /**
     * format
     */
    fun format(formatter: SimpleDateFormat, date: Date?): String? {
        return ifNull { formatter.format(date) }
    }

    /**
     * time parse
     */
    fun timeParse(ctx: Context, org: String?): Date? {
        return ifNull { DateFormat.getTimeFormat(ctx).parse(org) }
    }

    /**
     * time format
     */
    fun timeFormat(ctx: Context, date: Date?): String? {
        return ifNull { DateFormat.getTimeFormat(ctx).format(date) }
    }

    /**
     * date parse
     */
    fun dateParse(ctx: Context, org: String?): Date? {
        return ifNull { DateFormat.getDateFormat(ctx).parse(org) }
    }

    /**
     * date format
     */
    fun dateFormat(ctx: Context, date: Date?): String? {
        return ifNull { DateFormat.getDateFormat(ctx).format(date) }
    }

    /**
     * middle date parse
     */
    fun middleDateParse(ctx: Context, org: String?): Date? {
        return ifNull { DateFormat.getMediumDateFormat(ctx).parse(org) }
    }

    /**
     * middle date format
     */
    fun middleDateFormat(ctx: Context, date: Date?): String? {
        return ifNull { DateFormat.getMediumDateFormat(ctx).format(date) }
    }

    /**
     * long date parse
     */
    fun longDateParse(ctx: Context, org: String?): Date? {
        return ifNull { DateFormat.getLongDateFormat(ctx).parse(org) }
    }

    /**
     * long date format
     */
    fun longDateFormat(ctx: Context, date: Date?): String? {
        return ifNull { DateFormat.getLongDateFormat(ctx).format(date) }
    }

    // =================================================================

    fun long2Date(time: Long?): Date? {
        time ?: return null
        return ifNull { Date(time) }
    }

    fun string2Date(time: String?): Date? {
        return ifNull {
            val long = time?.toLong()
            long2Date(long)
        }
    }
}



