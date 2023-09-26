package com.manuelsoft.repository

import kotlinx.coroutines.flow.Flow


interface Repository {

    fun getAll(): Flow<List<Product>>

    suspend fun getById(id: Int): Product?

    suspend fun add(product: Product)

    suspend fun addList(products: List<Product>)

    suspend fun deleteById(id: Int)

    suspend fun deleteAll()

    suspend fun getSize(): Int

}