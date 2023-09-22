package com.manuelsoft.repository.impl

import com.manuelsoft.data.ProductEntity
import com.manuelsoft.data.ProductEntityDao
import com.manuelsoft.repository.Product
import com.manuelsoft.repository.Repository
import javax.inject.Inject

internal class RepositoryImpl @Inject constructor(private val productEntityDao: ProductEntityDao) : Repository {

    private fun productEntityToProduct(productEntity: ProductEntity): Product {
        return Product(productEntity.id, productEntity.name, productEntity.price)
    }

    private fun productEntitiesListToProductsList(productEntities: List<ProductEntity>): List<Product> {
        return productEntities.map { productEntity ->
            Product(productEntity.id, productEntity.name, productEntity.price)
        }
    }

    private fun productToProductEntity(product: Product): ProductEntity {
        return ProductEntity(product.id, product.name, product.price)
    }

    private fun productsListToProductEntitiesList(products: List<Product>): List<ProductEntity> {
        return products.map { product ->
            ProductEntity(product.id, product.name, product.price)
        }
    }

    override suspend fun getAll(): List<Product> {
        return productEntitiesListToProductsList(productEntityDao.getAll())
    }

    override suspend fun add(product: Product) {
        productEntityDao.insert(productToProductEntity(product))
    }


}