package com.manuelsoft.repository



interface Repository {

    suspend fun getAll(): List<Product>

    suspend fun getById(id: Int): Product?

    suspend fun add(product: Product)

    suspend fun addList(products: List<Product>)

    suspend fun deleteById(id: Int)

    suspend fun getSize(): Int

}