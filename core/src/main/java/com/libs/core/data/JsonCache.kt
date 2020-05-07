package com.libs.core.data

import com.google.gson.Gson
import com.orm.SugarRecord

/**
 * The data class to cache json string which include key and json string.
 */
data class JsonCache(val key: String, var data: String): SugarRecord()

/**
 * The object class to manage cached json string.
 */
object JsonManager

/**
 * Find
 *
 * @param key The key of related json cache.
 * @return A [JsonCache] instance which included key and cached json string.
 */
fun JsonManager.find(key: String): JsonCache? {
    return SugarRecord.find(
        JsonCache::class.java,
        "key = ?", key
    )?.firstOrNull()
}

/**
 * Get
 *
 * @param key The key of related json cache.
 * @return The cached json string.
 */
fun JsonManager.get(key: String): String? {
    return find(key)?.data
}

/**
 * Get
 *
 * @param key The key of related json cache.
 * @param clazz The [Class] of T which shall to be cached.
 * @return The instance of T.
 */
fun <T> JsonManager.get(key: String,
                        clazz: Class<T>): T? {
    return get(key)?.let {
        Gson().fromJson(
            it,
            clazz
        )
    }
}

/**
 * Put
 *
 * @param key The key of related json cache.
 * @param data The json string to be cached.
 */
fun JsonManager.put(key: String,
                    data: String) {
    find(key)?.let {
        it.data = data
        it.save()
    } ?: Unit.let {
        JsonCache(
            key,
            data
        ).save()
    }
}

/**
 * Put
 *
 * @param key The key of related json cache.
 * @param data The instance of T which shall to be cached.
 */
fun <T> JsonManager.put(key: String,
                        data: T) {
    find(key)?.let {
        it.data = Gson().toJson(data)
        it.save()
    } ?: Unit.let {
        JsonCache(
            key,
            Gson().toJson(data)
        ).save()
    }
}

