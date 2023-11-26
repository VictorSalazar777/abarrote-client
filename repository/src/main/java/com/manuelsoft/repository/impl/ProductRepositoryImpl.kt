package com.manuelsoft.repository.impl

import android.database.sqlite.SQLiteException
import com.manuelsoft.data.ProductEntityDao
import com.manuelsoft.data.entity.ProductEntity
import com.manuelsoft.domain_model.model.Product
import com.manuelsoft.domain_repository.repository.ProductRepository
import com.manuelsoft.mylibrary.AbarroteProductService
import com.manuelsoft.mylibrary.ProductDto
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productEntityDao: ProductEntityDao,
    private val abarroteProductService: AbarroteProductService
) :
    ProductRepository {

    private fun productEntityToProduct(productEntity: ProductEntity): Product {
        return Product(
            productEntity.id,
            productEntity.name,
            productEntity.categoryId,
            productEntity.price
        )
    }

    private fun productEntitiesListToProductsList(productEntities: List<ProductEntity>): List<Product> {
        return productEntities.map { productEntity ->
            Product(
                productEntity.id,
                productEntity.name,
                productEntity.categoryId,
                productEntity.price
            )
        }
    }

    private fun productToProductDto(product: Product): ProductDto {
        return ProductDto(product.id, product.name, product.categoryId, product.price)
    }

    private fun productDtoToProductEntity(productDto: ProductDto): ProductEntity {
        return ProductEntity(
            productDto.id,
            productDto.name,
            productDto.categoryId,
            productDto.price
        )
    }


    override suspend fun getAll(): Result<List<Product>> {

        return try {
            val productEntityListResult = productEntityDao.getAll()
            productEntityListResult.map { productEntity ->
                productEntityToProduct(productEntity)
            }.let { Result.success(it) }

        } catch (e: SQLiteException) {
            Result.failure(e)
        }

    }

    override suspend fun getById(id: Int): Result<Product?> {

        return try {
            val productEntity = productEntityDao.getById(id)

            if (productEntity != null) {
                Result.success(productEntityToProduct(productEntity))
            } else {
                Result.success(null)
            }

        } catch (e: SQLiteException) {
            Result.failure(e)
        }

    }

    override suspend fun add(product: Product): Result<Unit> {

        val result = abarroteProductService.createProduct(productToProductDto(product))

        return if (result.isSuccess) {
            try {
                productEntityDao.addProduct(productDtoToProductEntity(result.getOrNull()!!))
                Result.success(Unit)
            } catch (e: SQLiteException) {
                Result.failure(e)
            }

        } else {
            Result.failure(result.exceptionOrNull()!!)
        }

    }

    override suspend fun updateProduct(product: Product): Result<Unit> {
        val productDto = productToProductDto(product)
        val result = abarroteProductService.updateProduct(productDto.id, productDto)
        return if (result.isSuccess) {
            try {
                productEntityDao.updateProduct(productDtoToProductEntity(result.getOrNull()!!))
                Result.success(Unit)
            } catch (e: SQLiteException) {
                Result.failure(e)
            }
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }

    override suspend fun deleteById(id: Int): Result<Unit> {
        val result = abarroteProductService.deleteProduct(id)
        return if (result.isSuccess) {
            productEntityDao.deleteById(id)
            Result.success(Unit)
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }


    override suspend fun getSize(): Result<Int> {
        return try {
            Result.success(productEntityDao.getSize())
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }


}