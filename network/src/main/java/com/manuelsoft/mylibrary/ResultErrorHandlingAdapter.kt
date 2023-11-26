package com.manuelsoft.mylibrary

import kotlinx.coroutines.CancellationException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultErrorHandlingAdapter() :
    CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }
        check(returnType is ParameterizedType) { "ResultCall must have generic type (e.g., ResultCall<ResponseBody>)" }

        // Result<T>
        val resultType = getParameterUpperBound(0, returnType)


        return if ((resultType is ParameterizedType) && (resultType.rawType == Result::class.java)) {

            object : CallAdapter<Any, Call<Result<*>>> {
                override fun responseType(): Type {
                    val dataType = getParameterUpperBound(0, resultType)
                    return dataType
                }

                override fun adapt(call: Call<Any>): Call<Result<*>> {
                    return ResultCall(call) as Call<Result<*>>
                }

            }
        } else {
            return null
        }
    }
}

class ResultCall<T>(private val delegate: Call<T>) :
    Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {

        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                when (response.code()) {
                    in 200..299 -> {
                        val myResult = Result.success(response.body()!!)
                        callback.onResponse(this@ResultCall, Response.success(myResult))

                    }

                    // https://stackoverflow.com/questions/212239/what-java-exception-class-to-use-for-http-errors

                    400 -> {
                        val myResult = Result.failure<T>(HTTPBadRequestException(response))
                        callback.onResponse(this@ResultCall, Response.success(myResult))
                    }

                    401 -> {
                        val myResult = Result.failure<T>(HTTPUnauthenticatedException(response))
                        callback.onResponse(this@ResultCall, Response.success(myResult))

                    }

                    403 -> {
                        val myResult = Result.failure<T>(HTTPForbiddenException(response))
                        callback.onResponse(this@ResultCall, Response.success(myResult))
                    }

                    404 -> {
                        val myResult = Result.failure<T>(HTTPNotFoundException(response))
                        callback.onResponse(this@ResultCall, Response.success(myResult))
                    }


                    in 405..499 -> {
                        val myResult = Result.failure<T>(HTTPClientException(response))
                        callback.onResponse(this@ResultCall, Response.success(myResult))

                    }

                    in 500..599 -> {
                        val myResult = Result.failure<T>(HTTPServerException(response))
                        callback.onResponse(this@ResultCall, Response.success(myResult))

                    }

                    else -> {
                        val myResult = Result.failure<T>(HTTPAnotherException(response))
                        callback.onResponse(this@ResultCall, Response.success(myResult))

                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                when (t) {
                    is IOException -> {
                        val myResult = Result.failure<T>(IOException(t))
                        callback.onResponse(this@ResultCall, Response.success(myResult))
                    }

                    is Exception -> {
                        if (t is CancellationException) {
                            throw t
                        }
                        val myResult = Result.failure<T>(UnexpectedException(t))
                        callback.onResponse(this@ResultCall, Response.success(myResult))
                    }
                }
            }

        })

    }


    override fun clone(): Call<Result<T>> {
        return ResultCall(delegate.clone())
    }

    override fun execute(): Response<Result<T>> {
        try {
            val response = delegate.execute()
            val code: Int = response.code()
            if (code in 200..299) {
                return Response.success(Result.success(response.body()!!))
            } else if (code == 401) {
                return Response.success(
                    Result.failure(HTTPUnauthenticatedException(response))
                )
            } else if (code in 400..499) {
                return Response.success(
                    Result.failure(HTTPClientException(response))
                )
            } else if (code in 500..599) {
                return Response.success(
                    Result.failure(HTTPServerException(response))
                )
            } else {
                return Response.success(
                    Result.failure(HTTPAnotherException(response))
                )
            }
        } catch (e: IOException) {
            return Response.success(
                Result.failure(IOException(e))
            )
        } catch (e: Exception) {
            return Response.success(
                Result.failure(UnexpectedException(e))
            )
        }
    }

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }

}

class HTTPBadRequestException(response: Response<*>) : HttpException(response)

class HTTPUnauthenticatedException(response: Response<*>) : HttpException(response)

class HTTPForbiddenException(response: Response<*>) : HttpException(response)

class HTTPNotFoundException(response: Response<*>) : HttpException(response)

class HTTPClientException(response: Response<*>) : HttpException(response)

class HTTPServerException(response: Response<*>) : HttpException(response)

class HTTPAnotherException(response: Response<*>) : HttpException(response)

class UnexpectedException(t: Throwable) : Exception(t)