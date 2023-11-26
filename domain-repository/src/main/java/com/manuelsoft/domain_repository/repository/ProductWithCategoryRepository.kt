package com.manuelsoft.domain_repository.repository

import com.manuelsoft.domain_model.model.ProductWithCategory
import kotlinx.coroutines.flow.Flow

interface ProductWithCategoryRepository {

    suspend fun getAll(): Flow<Result<List<ProductWithCategory>>>

    suspend fun getById(id: Int): Result<ProductWithCategory?>

    suspend fun getSize(): Result<Int>

}