package com.libs.core.net

import com.google.gson.Gson
import com.libs.core.data.JsonCreator
import com.libs.core.util.Loger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Abstract rest api for config retrofit and get instance.
 * @see Retrofit
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class AbsRestApi {

    abstract var baseUrl: String

    open var debug: Boolean = false

    open var loger: Loger? = null

    open var gson: Gson? = null

    open var okHttpClient: OkHttpClient? = null

    open var interceptors: List<Interceptor>? = null

    open var converterFactory: Converter.Factory? = null

    open var callAdapterFactory: CallAdapter.Factory? = null

    /**
     * To get a configuration retrofit instance.
     */
    open val retrofit: Retrofit
        get() = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            okHttpClient?.let { client(it) }
            converterFactory?.let { addConverterFactory(it) }
            callAdapterFactory?.let { addCallAdapterFactory(it) }
        }.build()

}

/**
 * Simple rest api to inherit from AbsRestApi.
 * This subclass shall have normally configuration
 * like okHttpClient / gSon / convertFactory .etc
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class SimpleRestApi: AbsRestApi() {

    companion object {
        private const val TAG = "SimpleRestApi"
    }

    final override var debug: Boolean = true

    override var loger: Loger? = Loger(TAG, debug)

    final override var gson: Gson? = JsonCreator().create()

    override var okHttpClient: OkHttpClient? = OkHttpClient().newBuilder().apply {
        callTimeout(5, TimeUnit.SECONDS)
        connectTimeout(5, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)

        //addInterceptor(LoggingInterceptor(loger))
        addNetworkInterceptor(LoggingInterceptor(loger))
        interceptors?.forEach { addInterceptor(it) }
    }.build()

    override var converterFactory: Converter.Factory? = GsonConverterFactory.create(gson)
}

/**
 * Log interceptor to logging the message of request api program.
 * @param log
 */
open class LoggingInterceptor(private val log: Loger? = null): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        log?.d("=".repeat(30))
        log?.d("request headers >> " + request.headers().toString())
        log?.d("request key     >> " + request.url().toString())
        log?.d("request body    >> " + request.body().toString())

        log?.d("\n")

        val response = chain.proceed(request)

        log?.d("response message >> " + response.message())
        log?.d("response code    >> " + response.code())
        log?.d("response body    >> " + response.body().toString())
        log?.d("=".repeat(30))

        return response
    }

}

