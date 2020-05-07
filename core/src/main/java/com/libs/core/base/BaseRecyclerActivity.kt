package com.libs.core.base

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.libs.core.adapter.QuickAdapter
import com.libs.core.data.PageHolder
import com.libs.core.recycler.init
import kotlinx.android.synthetic.main.common_recycler.recyclerView
import kotlinx.android.synthetic.main.common_swipe_recycler.swipeRefresh

/**
 * NOTE: Use 'common_recycler.xml' layout.
 */
abstract class BaseRecyclerActivity<T, VH: BaseViewHolder>: PermissionActivity() {

    /**
     * The activity content id.
     */
    abstract val layoutId: Int

    abstract val layoutManager  : RecyclerView.LayoutManager
    abstract fun createAdapter(): QuickAdapter<T, VH>

    lateinit var mAdapter: QuickAdapter<T, VH>
    lateinit var mRecyclerView: RecyclerView
    var loadMoreModule: BaseLoadMoreModule? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        // 1. find recycler view
        mRecyclerView = recyclerView

        // 2. create adapter
        mAdapter = createAdapter()

        // 3. init recycler view
        mRecyclerView.init(layoutManager, mAdapter)

        loadMoreModule = mAdapter.loadMoreModule?.apply {
            isEnableLoadMore = false
        }
    }
}

/**
 * NOTE: Use 'common_swipe_recycler.xml' layout.
 */
abstract class BaseSwipeRecyclerActivity<T, VH: BaseViewHolder>: BaseRecyclerActivity<T, VH>() {

    lateinit var mSwipeRefresh: SwipeRefreshLayout
    var pageHolder = PageHolder<T>()

    /**
     * Refresh net data
     * @param callback
     */
    abstract fun reqData(pageIndex: Int, callback: (MutableList<T>?) -> Unit)

    /**
     * @see SwipeRefreshLayout.OnRefreshListener
     */
    open fun onRefresh() {
        reqData(1) {
            toggleRefreshing()

            it?.let {
                pageHolder.refresh(it)
                mAdapter  .notifyDataSetChanged()
            }

            // Write cache here if need
        }
    }

    /**
     * Refresh
     */
    fun refresh() {
        toggleRefreshing()
        onRefresh()
    }

    /**
     * Toggle the swipe refresh status.
     */
    fun toggleRefreshing() {
        mSwipeRefresh.isRefreshing = !mSwipeRefresh.isRefreshing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. find swipe refresh
        mSwipeRefresh = swipeRefresh

        // 2. set swipe refresh
        mSwipeRefresh.setOnRefreshListener {
            onRefresh()
        }

        // =======
        // initial
        // =======
        mAdapter.setNewData(pageHolder.items)

        refresh()

        loadMoreModule?.run {
            // 1) configuration
            isEnableLoadMore = true
            isEnableLoadMoreIfNotFullPage = false

            // 2) load more
            setOnLoadMoreListener {
                reqData(pageHolder.pageIndex) {
                    it?.let {
                        pageHolder.load(it)
                        mAdapter  .notifyDataSetChanged()

                        loadMoreComplete()
                    }
                        ?: loadMoreFail ()
                }
            }
        }

        // Read cache here if need
    }

}