package com.manuelsoft.mylibrary

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    val client = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.3:8080/abarrote/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(ResultErrorHandlingAdapter())
            .client(client)
            .build()
    }

    fun getRetrofitService(): AbarroteCategoryService {
        return retrofit.create(AbarroteCategoryService::class.java)
    }


    @Test
    fun service() = runBlocking {
        val categoryDtoResult =
            getRetrofitService().getAllCategories()
        assertTrue(categoryDtoResult.isSuccess)

    }
}

internal class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        Log.i(
            "TAG",
            String.format(
                "Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()
            )
        )
        val response: Response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.i(
            "TAG",
            String.format(
                "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers()
            )
        )
        return response
    }
}