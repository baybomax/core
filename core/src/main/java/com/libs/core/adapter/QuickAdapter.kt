package com.libs.core.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *
 */

// https://github.com/CymChad/BaseRecyclerViewAdapterHelper/blob/master/readme/1-BaseQuickAdapter.md

abstract class QuickAdapter<T, VH: BaseViewHolder>(
    @LayoutRes layoutResId: Int,
    data: MutableList<T>? = null
): BaseQuickAdapter<T, VH>(layoutResId, data), LoadMoreModule
