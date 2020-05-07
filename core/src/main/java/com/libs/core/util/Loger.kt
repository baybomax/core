package com.libs.core.util

import android.util.Log

/**
 * Log.
 *
 * @param TAG The tag flag string, default is `Log`
 * @param debug True if debug mode, false otherwise, default is true
 */
class Loger(var TAG  : String  = "Log", var debug: Boolean = true ) {

    /**
     * <li>
     *     1. d: debug
     *     2. w: warning
     *     3. e: error
     *     4. i: info
     *     5. v: verbose
     * </li>
     * @param msg The message to log.
     */
    fun d(msg: String) { if (debug) { Log.d(TAG, msg) } }
    fun w(msg: String) { if (debug) { Log.w(TAG, msg) } }
    fun e(msg: String) { if (debug) { Log.e(TAG, msg) } }
    fun i(msg: String) { if (debug) { Log.i(TAG, msg) } }
    fun v(msg: String) { if (debug) { Log.v(TAG, msg) } }
}
