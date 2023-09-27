package com.manuelsoft.repository.impl

import com.manuelsoft.data.ProductEntity
import com.manuelsoft.data.ProductEntityDao
import com.manuelsoft.repository.Product
import com.manuelsoft.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RepositoryImpl @Inject constructor(private val productEntityDao: ProductEntityDao) :
    Repository {

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

    override fun getAll(): Flow<List<Product>> {

        return productEntityDao.getAll().map { productEntitiesList ->
            productEntitiesListToProductsList(productEntitiesList)
        }
    }

    override suspend fun getById(id: Int): Product? {

        val productEntity = productEntityDao.getById(id)
        if (productEntity == null) {
            return null
        } else {
            return productEntityToProduct(productEntity)
        }
    }

    override suspend fun add(product: Product): Long {
        return productEntityDao.insert(productToProductEntity(product))
    }

    override suspend fun addList(products: List<Product>): LongArray {
        return productEntityDao.insert(productsListToProductEntitiesList(products))
    }

    override suspend fun updateName(id: Int, name: String): Int {
        return productEntityDao.updateName(id, name)
    }

    override suspend fun updatePrice(id: Int, price: Float): Int {
        return productEntityDao.updatePrice(id, price)
    }

    override suspend fun updateProduct(product: Product): Int {
        return productEntityDao.updateProduct(productToProductEntity(product))
    }

    override suspend fun deleteById(id: Int): Int {
        return productEntityDao.deleteById(id)
    }

    override suspend fun deleteAll(): Int {
        return productEntityDao.deleteAll()
    }

    override suspend fun getSize(): Int {
        return productEntityDao.getSize()
    }


}