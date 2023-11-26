package com.manuelsoft.repository.impl

import android.database.sqlite.SQLiteException
import androidx.room.withTransaction
import com.manuelsoft.data.CategoryEntityDao
import com.manuelsoft.data.ProductEntityDao
import com.manuelsoft.data.ProductWithCategoryDao
import com.manuelsoft.data.RoomDb
import com.manuelsoft.data.entity.CategoryEntity
import com.manuelsoft.data.entity.ProductEntity
import com.manuelsoft.domain_repository.repository.RepositoryUpdater
import com.manuelsoft.mylibrary.AbarroteCategoryService
import com.manuelsoft.mylibrary.AbarroteProductService
import com.manuelsoft.mylibrary.CategoryDto
import com.manuelsoft.mylibrary.ProductDto
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class RepositoryUpdaterImpl @Inject constructor(
    private val abarroteProductService: AbarroteProductService,
    private val abarroteCategoryService: AbarroteCategoryService,
    private val productEntityDao: ProductEntityDao,
    private val categoryEntityDao: CategoryEntityDao,
    private val productWithCategoryEntityDao: ProductWithCategoryDao,
    private val roomDb: RoomDb
) : RepositoryUpdater {

    private fun productsDtoToProductsEntityMapper(productsDto: List<ProductDto>): List<ProductEntity> {
        val list = mutableListOf<ProductEntity>()

        productsDto.forEach {
            val productEntity = ProductEntity(it.id, it.name, it.categoryId, it.price)
            list.add(productEntity)
        }

        return list
    }

    private fun categoriesDtoToCategoriesEntityMapper(categoriesDto: List<CategoryDto>): List<CategoryEntity> {
        val list = mutableListOf<CategoryEntity>()

        categoriesDto.forEach {
            val categoryEntity = CategoryEntity(it.id, it.name)
            list.add(categoryEntity)
        }

        return list
    }

    override suspend fun update(): Result<Unit> = coroutineScope {
        val deferredProductsDtoResult = async { abarroteProductService.getAllProducts() }
        val deferredCategoriesDtoResult = async { abarroteCategoryService.getAllCategories() }

        val results = Pair(deferredProductsDtoResult.await(), deferredCategoriesDtoResult.await())

        val productsDtoResult = results.first
        val categoriesDtoResult = results.second

        if (productsDtoResult.isFailure) {
            return@coroutineScope Result.failure(productsDtoResult.exceptionOrNull()!!)
        }

        if (categoriesDtoResult.isFailure) {
            return@coroutineScope Result.failure(categoriesDtoResult.exceptionOrNull()!!)
        }

        val productsDto = productsDtoResult.getOrNull()!!
        val categoriesDto = categoriesDtoResult.getOrNull()!!
        val productsEntity = productsDtoToProductsEntityMapper(productsDto)
        val categoriesEntity = categoriesDtoToCategoriesEntityMapper(categoriesDto)


        return@coroutineScope try {
            roomDb.withTransaction {
                productEntityDao.deleteAll()
                categoryEntityDao.deleteAll()
                productEntityDao.addProducts(productsEntity)
                categoryEntityDao.addCategories(categoriesEntity)
                productWithCategoryEntityDao.deleteAll()
                productWithCategoryEntityDao.insert()
            }

            Result.success(Unit)
        } catch (e: SQLiteException) {
            Result.failure(e)
        }


    }

}
