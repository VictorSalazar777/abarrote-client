package com.manuelsoft.repository

import kotlinx.coroutines.flow.Flow


interface Repository {

    fun getAll(): Flow<List<Product>>

    suspend fun getById(id: Int): Product?

    suspend fun add(product: Product): Long

    suspend fun addList(products: List<Product>): LongArray

    suspend fun updateName(id: Int, name: String): Int

    suspend fun updatePrice(id: Int, price: Float): Int

    suspend fun updateProduct(product: Product): Int

    suspend fun deleteById(id: Int): Int

    suspend fun deleteAll(): Int

    suspend fun getSize(): Int

}