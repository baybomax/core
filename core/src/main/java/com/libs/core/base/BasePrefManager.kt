package com.libs.core.base

import android.annotation.SuppressLint
import android.content.Context

/**
 * The share preference to store key-value.
 * @param mContext The context.
 */
@SuppressLint("ApplySharedPref")
open class BasePrefManager(protected val mContext: Context) {

    ////////////////////////////////////
    // Generic save/retrieve methods. //
    ////////////////////////////////////

    /**
     * Save string value.
     */
    fun save(spName: String, key: String, value: String) {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * Save boolean value.
     */
    fun save(spName: String, key: String, value: Boolean) {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     * Save long value.
     */
    fun save(spName: String, key: String, value: Long) {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    /**
     * Save float value.
     */
    fun save(spName: String, key: String, value: Float) {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putFloat(key, value)
        editor.commit()
    }

    /**
     * Save int value.
     */
    fun save(spName: String, key: String, value: Int) {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    /**
     * Save stringSet value.
     */
    fun save(spName: String, key: String, value: MutableSet<String>) {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putStringSet(key, value)
        editor.commit()
    }

    /**
     * Get string value.
     */
    fun getString(spName: String, key: String): String? {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return pref.getString(key, null)
    }

    /**
     * Get boolean value.
     */
    fun getBoolean(spName: String, key: String, defVal: Boolean): Boolean {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return pref.getBoolean(key, defVal)
    }

    /**
     * Get float value.
     */
    fun getFloat(spName: String, key: String, defVal: Float): Float {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return pref.getFloat(key, defVal)
    }

    /**
     * Get long value.
     */
    fun getLong(spName: String, key: String, defVal: Long): Long {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return pref.getLong(key, defVal)
    }

    /**
     * Get int value.
     */
    fun getInt(spName: String, key: String, defVal: Int): Int {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return pref.getInt(key, defVal)
    }

    /**
     * Get stringSet value.
     */
    fun getStringSet(spName: String, key: String, defVal: MutableSet<String>?): MutableSet<String>? {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return pref.getStringSet(key, defVal)
    }

    /**
     * Remove.
     */
    fun remove(spName: String, key: String) {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.remove(key)
        editor.commit()
    }

    @JvmOverloads
    fun getIncrementalLong(spName: String, key: String, step: Long = 1L): Long {
        val pref = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        var `val` = pref.getLong(key, 0)
        `val` += step
        editor.putLong(key, `val`)
        editor.commit()
        return `val`
    }

}
