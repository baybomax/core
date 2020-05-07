package com.libs.core.net

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The abs class of api caller to handle api.
 * With a function <enqueue> to send request.
 * With the data struct <T>.
 */
@Suppress("LeakingThis")
abstract class AbsApiCaller<T> {

    open var restApi: AbsRestApi? = null

    abstract var clazz: Class<T>
    open var service: T? = restApi?.retrofit?.create(clazz)

//    /**
//     * Get the specified observable interface
//     * @see Call<T>
//     */
//    abstract fun getCall(): Call<T>?

    /**
     * Really send a request for api call.
     *
     * @see Call in 'Retrofit' api.
     * @param call a call instance.
     * @param callback a callback.
     */
    open fun <T> enqueue(call: Call<T>?, callback: ((T?) -> Unit)?) {
        call?.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                t.printStackTrace()
                callback?.invoke(call.execute().body())
            }
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback?.invoke(response.body())
            }
        })
    }

//    /**
//     * Generate the result of api call
//     * @see BaseEntity<T>
//     * @see Response<T>
//     * @param response
//     */
//    @Deprecated("Unknown base entity struct!")
//    private fun <T> genBaseEntity(response: Response<T>): BaseEntity {
//        return BaseEntity<T>().apply {
//            code = response.code()
//            msg  = response.message()
//            data = response.body()
//        }
//    }

}


