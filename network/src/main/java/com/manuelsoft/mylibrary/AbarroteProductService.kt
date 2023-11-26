package com.manuelsoft.mylibrary

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AbarroteProductService {

    @GET("products")
    suspend fun getAllProducts(): Result<List<ProductDto>>

    @POST("products")
    suspend fun createProduct(@Body productDto: ProductDto): Result<ProductDto>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body productDto: ProductDto): Result<ProductDto>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Result<String>

}