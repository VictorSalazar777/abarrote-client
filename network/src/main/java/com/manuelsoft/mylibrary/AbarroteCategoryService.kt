package com.manuelsoft.mylibrary

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AbarroteCategoryService {

    @GET("categories")
    suspend fun getAllCategories(): Result<List<CategoryDto>>

    @POST("categories")
    suspend fun createCategory(@Body categoryDto: CategoryDto): Result<CategoryDto>

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body categoryDto: CategoryDto
    ): Result<CategoryDto>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int): Result<String>

}