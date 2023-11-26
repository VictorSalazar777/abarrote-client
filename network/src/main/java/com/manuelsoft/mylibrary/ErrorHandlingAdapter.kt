package com.manuelsoft.mylibrary

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor


/**
 * A sample showing a custom {@link CallAdapter} which adapts the built-in {@link Call} to a custom
 * version whose callback has more granular methods.
 */
class ErrorHandlingAdapter {
    /** A callback which offers granular callbacks for various conditions. */
    interface MyCallback<T> {
        /** Called for [200, 300) responses. */
        fun success(response: Response<T>)

        /** Called for 401 responses. */
        fun unauthenticated(response: Response<*>)

        /** Called for [400, 500) responses, except 401. */

        fun clientError(response: Response<*>)

        /** Called for [500, 600) response. */
        fun serverError(response: Response<*>)

        /** Called for network errors while making the call. */
        fun networkError(e: IOException)

        /** Called for unexpected errors while making the call. */
        fun unexpectedError(t: Throwable)
    }

    interface MyCall<T> {
        fun cancel()

        fun enqueue(callback: MyCallback<T>)

        fun clone(): MyCall<T>

        // Left as an exercise for the reader...
        // TODO MyResponse<T> execute() throws MyHttpException;
    }

    class ErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
        override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): CallAdapter<*, *>? {
            if (getRawType(returnType) != MyCall::class.java) {
                return null
            }
            check(returnType is ParameterizedType) { "MyCall must have generic type (e.g., MyCall<ResponseBody>)" }
            val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
            val callbackExecutor = retrofit.callbackExecutor()
            return ErrorHandlingCallAdapter<R>(responseType, callbackExecutor)
        }

    }


    private class ErrorHandlingCallAdapter<R>(
        val responseType: Type,
        val callbackExecutor: Executor?
    ) : CallAdapter<R, MyCall<R>> {

        override fun responseType(): Type {
            return responseType
        }

        override fun adapt(call: Call<R>): MyCall<R> {
            return MyCallAdapter<R>(call, callbackExecutor)
        }

    }
}


/** Adapts a {@link Call} to {@link MyCall}. */
class MyCallAdapter<T>(private val call: Call<T>, private val callbackExecutor: Executor?) :
    ErrorHandlingAdapter.MyCall<T> {

    override fun cancel() {
        call.cancel()
    }

    override fun clone(): ErrorHandlingAdapter.MyCall<T> {
        return MyCallAdapter<T>(call.clone(), callbackExecutor)
    }

    override fun enqueue(callback: ErrorHandlingAdapter.MyCallback<T>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {

                if (callbackExecutor != null) {
                    callbackExecutor.execute {
                        executeCallbacks(response, callback)
                    }
                } else {
                    executeCallbacks(response, callback)
                }

            }

            private fun executeCallbacks(
                response: Response<T>,
                callback: ErrorHandlingAdapter.MyCallback<T>
            ) {
                val code: Int = response.code()
                if (code in 200..299) {
                    callback.success(response)
                } else if (code == 401) {
                    callback.unauthenticated(response)
                } else if (code in 400..499) {
                    callback.clientError(response)
                } else if (code in 500..599) {
                    callback.serverError(response)
                } else {
                    callback.unexpectedError(RuntimeException("Unexpected response $response"))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {

                if (callbackExecutor != null) {
                    callbackExecutor.execute {
                        executeFailureCallbacks(call, t)
                    }
                } else {
                    executeFailureCallbacks(call, t)
                }

            }

            private fun executeFailureCallbacks(call: Call<T>, t: Throwable) {
                if (t is IOException) {
                    callback.networkError(t as IOException)
                } else {
                    callback.unexpectedError(t)
                }
            }

        })
    }

}

interface HttpBinService {
    @GET("/ip")
    fun getIp(): ErrorHandlingAdapter.MyCall<Ip>;
}

class Ip {
    lateinit var origin: String
}
