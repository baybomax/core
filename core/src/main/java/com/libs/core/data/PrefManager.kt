package com.libs.core.data

import android.content.Context
import com.libs.core.base.BasePrefManager
import kotlin.reflect.KProperty

/**
 * The preference manager to convenient store key-value.
 * @param context The context.
 */
open class PrefManager(context: Context) : BasePrefManager(context) {

    @Suppress("UNCHECKED_CAST")
    open inner class SPDelegate<T>(val clazz: Class<T>,
                                   private val spName: String,
                                   private val key: String,
                                   private val defValue: T? = null) {

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return when (defValue) {
                is String, null -> getString (spName, key          ) ?: defValue
                is Boolean      -> getBoolean(spName, key, defValue)
                is Long         -> getLong   (spName, key, defValue)
                is Float        -> getFloat  (spName, key, defValue)
                is Int          -> getInt    (spName, key, defValue)
                else            -> null
            } as T?
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            value?.let {
                when (it) {
                    is String  -> save(spName, key, it)
                    is Boolean -> save(spName, key, it)
                    is Long    -> save(spName, key, it)
                    is Float   -> save(spName, key, it)
                    is Int     -> save(spName, key, it)
                    else -> {
                    }
                }
            }
        }
    }

    /**
     * Notice that if manage Boolean/Long/Float/Int preference must transfer 'defValue'
     */
    inline fun <reified T> spd(spName: String, key: String, defValue: T? = null): SPDelegate<T> {
        return SPDelegate(T::class.java, spName, key, defValue)
    }
}
