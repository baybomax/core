package com.libs.core.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.libs.core.rx.RxBusPassenger
import com.libs.core.util.visibleOrInvisible

///**
// * Event class to indicate a fragment has finished create view.
// */
//data class FragmentViewCreatedEvent(val fragment: BaseFragment, val view: View?): IEvent

open class BaseFragment: Fragment() {

    open val eventDefines: RxBusPassenger = RxBusPassenger()

    /**
     * Called to do initial creation of a fragment.  This is called after
     * [.onAttach] and before
     * [.onCreateView].
     *
     *
     * Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see [.onActivityCreated].
     *
     *
     * Any restored child fragments will be created before the base
     * `Fragment.onCreate` method returns.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventDefines.onboardBus()
    }

//    /**
//     * Called immediately after [.onCreateView]
//     * has returned, but before any saved state has been restored in to the view.
//     * This gives subclasses a chance to initialize themselves once
//     * they know their view hierarchy has been completely created.  The fragment's
//     * view hierarchy is not however attached to its parent at this point.
//     * @param view The View returned by [.onCreateView].
//     * @param savedInstanceState If non-null, this fragment is being re-constructed
//     * from a previous saved state as given here.
//     */
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        RxBus.post(FragmentViewCreatedEvent(this, view))
//    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after [.onStop] and before [.onDetach].
     */
    override fun onDestroy() {
        eventDefines.leaveBus()
        super.onDestroy()
    }
}

/**
 * Show or hide a fragment,
 * @param visible True if show fragment view, false otherwise.
 */
fun BaseFragment.visible(visible: Boolean) = run {
    view?.visibility = visible.visibleOrInvisible
}
