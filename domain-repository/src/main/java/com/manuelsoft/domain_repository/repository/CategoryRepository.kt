package com.manuelsoft.domain_repository.repository

import com.manuelsoft.domain_model.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAll(): Flow<Result<List<Category>>>

    suspend fun getById(id: Int): Result<Category?>

    suspend fun add(category: Category): Result<Unit>

    suspend fun updateCategory(category: Category): Result<Unit>

    suspend fun deleteById(id: Int): Result<Unit>

    suspend fun getSize(): Result<Int>
}