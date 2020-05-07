package com.libs.core.net

import com.libs.core.data.BaseEntity
import retrofit2.Call
import retrofit2.http.Query

/**
 *
 */

// ==========================================================

/**
 * The test rest extend [SimpleRestApi] or custom extend from [AbsRestApi]
 * @see SimpleRestApi
 * @see AbsRestApi
 */
@Deprecated("Just for test example")
open class TestRestApi: SimpleRestApi() {
    override var baseUrl: String = "" // TODO: Real use in project must not non!
}

@Deprecated("Just for test example")
abstract class ApiCaller<T>: AbsApiCaller<T>() {
    override var restApi: AbsRestApi? = TestRestApi() // NOTE: With 'TestRestApi'
}

// ==========================================================
// ==========================================================

@Deprecated("Just for test example")
class TestCaller: ApiCaller<TestService>() {
    override var clazz: Class<TestService> = TestService::class.java
}

@Deprecated("Just for test example")
interface TestService {
    fun test1(@Query("id") id: String): Call<BaseEntity>
    fun test2(@Query("id") id: String, @Query("name") name: String): Call<BaseEntity>
}

@Deprecated("Just for test example")
class TestApi {
    val caller = TestCaller()

    fun test1() {
        caller.enqueue(caller.service?.test1("")) {
        }
    }

    fun test2() {
        caller.enqueue(caller.service?.test2("", "")) {
        }
    }
}
