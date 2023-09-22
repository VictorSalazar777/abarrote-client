package com.manuelsoft.repository.impl

import com.manuelsoft.data.ProductEntity
import com.manuelsoft.data.ProductEntityDao
import com.manuelsoft.repository.Product
import com.manuelsoft.repository.Repository
import javax.inject.Inject

internal class RepositoryImpl @Inject constructor(private val productEntityDao: ProductEntityDao) : Repository {

    private fun productEntityToProduct(productEntity: ProductEntity): Product {

    }

    private fun productToProductEntity(product: Product): ProductEntity {

    }

    override suspend fun getAll(): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun add(product: Product) {
        TODO("Not yet implemented")
    }


}