package com.libs.core.extents

/**
 *
 */

fun <E> MutableList<E>.contains(e: E, block: (Boolean, E) -> Unit) = block(contains(e), e)

fun <E> MutableList<E>.addOrRemove(e: E) = run {
    contains(e) { has, e ->
        if (has) {
            remove(e)
        } else {
            add(e)
        }
    }
}
