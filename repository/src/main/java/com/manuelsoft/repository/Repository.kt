package com.manuelsoft.repository



interface Repository {

    suspend fun getAll(): List<Product>

    suspend fun add(product: Product)

}