package com.libs.core.rx

import android.util.Log
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.TimeUnit

/**
 * Mark of Event classes.
 */
interface IEvent

/**
 * A singleton of event bus.
 */
val RxBus = RxBusContext()

/**
 * The Event Bus singleton.
 * This bus can be considered a collection of Observables.
 */
class RxBusContext {
    /**
     *
     */
    private val subjectMap = HashMap<Class<*>, Subject<*>>()

    /**
     * Get a Rx subject of certain type.
     */
    @Synchronized
    operator fun <T: IEvent> get(clazz: Class<T>): Subject<T> {
        var anySubject = subjectMap[clazz]
        if (null == anySubject){
            anySubject = PublishSubject.create<T>()
            subjectMap[clazz] = anySubject
        }
        @Suppress("UNCHECKED_CAST")
        return anySubject as Subject<T>
    }

    fun <T: IEvent> registerBehaviorEvent(clazz: Class<T>) {
        subjectMap[clazz] = BehaviorSubject.create<T>()
    }

    private fun finalize(){
        subjectMap.values.forEach {
            it.onComplete()
        }
        subjectMap.clear()
    }

    /**
     * Reset a subject if it was completed or error occured.
     */
    fun <T: IEvent> reset(clazz: Class<T>) {
        subjectMap[clazz]?.onComplete()
        subjectMap[clazz] = PublishSubject.create<T>()
    }

    inline fun <reified T: IEvent> reset() {
        reset(T::class.java)
    }

    /**
     * Post an arbitrary object to corresponding Observable.
     */
    fun <T: IEvent> post(t: T) {
        this[t.javaClass].onNext(t)
    }

    fun <T: IEvent> postError(clazz: Class<T>, e: Throwable){
        get(clazz).onError(e)
    }

    fun <T: IEvent> postComplete(clazz: Class<T>){
        get(clazz).onComplete()
    }

    inline fun <reified T: IEvent> postError(e: Throwable){
        postError(T::class.java, e)
    }

    inline fun <reified T: IEvent> postComplete() {
        postComplete(T::class.java)
    }

    /**
     * Subscribe an observer of type T
     */
    fun <T: IEvent> sub(clazz: Class<T>, observer: Observer<T>) {
        get(clazz).subscribe(observer)
    }

    inline fun <reified T: IEvent> sub(observer: Observer<T>) {
        sub(T::class.java, observer)
    }
}

/**
 * Buffered observer
 */
open class BufferedObserver<T: IEvent>(
        private val clazz       : Class<T>,
        private var _next       : (T)->Unit,
        private var _complete   : (()->Unit)?           = null,
        private var _error      : ((Throwable)->Unit)?  = null,
        private var _subscribe  : ((Disposable)->Unit)? = null): Observer<T> {

    override fun onNext(t: T)               { _next.invoke(t) }
    override fun onComplete()               { _complete?.invoke() }
    override fun onError(e: Throwable)      { _error?.invoke(e) }
    override fun onSubscribe(d: Disposable) { _subscribe?.invoke(d) }

    fun next(handler: (T)->Unit): BufferedObserver<T> {
        _next = handler
        return this
    }

    fun complete(handler: ()->Unit): BufferedObserver<T> {
        _complete = handler
        return this
    }

    fun error(handler: (Throwable)->Unit): BufferedObserver<T> {
        _error = handler
        return this
    }

    fun subscribe(handler: (Disposable)->Unit): BufferedObserver<T> {
        _subscribe = handler
        return this
    }

    open fun subscribeTo(context: RxBusContext){
        context.sub(clazz, this)
    }
}

open class BufferedWrapperObserver<T: IEvent>(clazz: Class<T>, another: Observer<T>): BufferedObserver<T>(
        clazz,
        another::onNext,
        another::onComplete,
        another::onError,
        another::onSubscribe)

/**
 * Bus Passenger, who takes the bus.
 */
open class RxBusPassenger(val rxContext: RxBusContext = RxBus) {

    companion object {
        private val TAG = "RxBusPassenger"
    }

    private val builders = ArrayList<BufferedObserver<*>>()
    private val disposables = ArrayList<Disposable>()

    /**
     * Subscribe all observers registered to this Passenger
     */
    fun onboardBus() {
        for(b in builders){
            b.subscribeTo(rxContext)
        }
    }

    /**
     * Unsubscribe all observers
     */
    fun leaveBus() {
        for(d in disposables){
            Log.d(TAG, "disposing: ${d.javaClass}")
            d.dispose()
        }
        disposables.clear()
    }

    fun <T: IEvent> reg(clazz: Class<T>, onNext: (T)->Unit): BufferedObserver<T> {
        return object: BufferedObserver<T>(clazz, onNext){
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                disposables.add(d)
            }
        }.apply {
            builders.add(this)
        }
    }

    fun <T: IEvent> reg(clazz: Class<T>, another: Observer<T>): BufferedObserver<T> {
        return object: BufferedWrapperObserver<T>(clazz, another){
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                disposables.add(d)
            }
        }.apply {
            builders.add(this)
        }
    }

    fun <T: IEvent> reg(clazz: Class<T>, throttleMs: Long, onNext: (T)->Unit): BufferedObserver<T> {
        return object : BufferedObserver<T>(clazz, onNext){
            override fun subscribeTo(context: RxBusContext) {
                context[clazz].throttleFirst(throttleMs, TimeUnit.MILLISECONDS).subscribe(this)
            }
            override fun onSubscribe(d: Disposable) {
                super.onSubscribe(d)
                disposables.add(d)
            }
        }.apply {
            builders.add(this)
        }
    }

    inline fun <reified T: IEvent> reg(noinline onNext: (T)->Unit): BufferedObserver<T> {
        return reg(T::class.java, onNext)
    }

    inline fun <reified T: IEvent> reg(another: Observer<T>): BufferedObserver<T> {
        return reg(T::class.java, another)
    }

    inline fun <reified T: IEvent> reg(throttleMs: Long, noinline onNext: (T)->Unit): BufferedObserver<T> {
        return reg(T::class.java, throttleMs, onNext)
    }

}

