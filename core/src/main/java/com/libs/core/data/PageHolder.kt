package com.libs.core.data

/**
 * The holder to manage paged data.
 * With data struct <T>.
 */
open class PageHolder<T> {

    var items = mutableListOf<T>()

    open var pageSize : Int = 20
         var pageIndex: Int = 1

    /**
     * Reset the pageHolder setting.
     *
     *  1. clear items list.
     *  2. set pageIndex zero.
     *  3. set total zero.
     */
    open fun reset() {
        items.clear()
        pageIndex = 1
    }

    /**
     * Refresh the pageHolder items.
     *
     *  1. reset the pageHolder setting. @see reset()
     *  2. set items equals parameter items.
     *  3. set total equals parameter total.
     *  4. add pageIndex by step one.
     */
    open fun refresh(itms: MutableList<T>) {
        reset()
        items.addAll(itms)
        pageIndex++
    }

    /**
     * Load the pageHolder items.
     *
     *  1. set items equals parameter items.
     *  2. set total equals parameter total.
     *  3. add pageIndex by step one.
     */
    open fun load(itms: MutableList<T>) {
        items.addAll(itms)
        pageIndex++
    }

}

