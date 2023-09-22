package com.manuelsoft.repository.impl

import android.database.sqlite.SQLiteException
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
        try {
            return productEntitiesListToProductsList(productEntityDao.getAll())
        } catch (e: SQLiteException) {
            throw RuntimeException(e)
        }
    }

    override suspend fun getById(id: Int): Product? {
        try {
            val productEntity = productEntityDao.getById(id)
            if (productEntity == null) {
                return null
            } else {
                return productEntityToProduct(productEntity)
            }
        } catch (e: SQLiteException) {
            throw RuntimeException(e)
        }
    }

    override suspend fun add(product: Product) {
        try {
            productEntityDao.insert(productToProductEntity(product))
        } catch (e: SQLiteException) {
            throw RuntimeException(e)
        }
    }

    override suspend fun addList(products: List<Product>) {
        try {
            productEntityDao.insert(productsListToProductEntitiesList(products))
        } catch (e: SQLiteException) {
            throw RuntimeException(e)
        }
    }

    override suspend fun deleteById(id: Int) {
        try {
            productEntityDao.deleteById(id)
        } catch (e: SQLiteException) {
            throw RuntimeException(e)
        }
    }

    override suspend fun getSize(): Int {
        try {
            return productEntityDao.getSize()
        } catch (e: SQLiteException) {
            throw RuntimeException(e)
        }

    }


}