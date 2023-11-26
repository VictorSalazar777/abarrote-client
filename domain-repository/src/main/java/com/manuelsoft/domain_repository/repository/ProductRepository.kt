package com.manuelsoft.domain_repository.repository

import com.manuelsoft.domain_model.model.Product


interface ProductRepository {

    suspend fun getAll(): Result<List<Product>>

    suspend fun getById(id: Int): Result<Product?>

    suspend fun add(product: Product): Result<Unit>

    suspend fun updateProduct(product: Product): Result<Unit>

    suspend fun deleteById(id: Int): Result<Unit>

    suspend fun getSize(): Result<Int>

}