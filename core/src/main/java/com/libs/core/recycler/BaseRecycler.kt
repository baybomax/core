package com.libs.core.recycler

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Convenient func of [RecyclerView].init
 *
 * @param lm the layout manager
 * @param adapter the adapter
 */
fun <T, VH: BaseViewHolder> RecyclerView.init(
    lm: RecyclerView.LayoutManager,
    adapter: BaseQuickAdapter<T, VH>
) {
    // 1. set layout manager
    layoutManager = lm

    // 2. set adapter
    this.adapter = adapter
}
