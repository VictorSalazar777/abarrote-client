package com.manuelsoft.repository.impl

import android.database.sqlite.SQLiteException
import com.manuelsoft.data.ProductWithCategoryDao
import com.manuelsoft.data.entity.ProductWithCategoryEntity
import com.manuelsoft.domain_model.model.ProductWithCategory
import com.manuelsoft.domain_repository.repository.ProductWithCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class ProductWithCategoryRepositoryImpl
@Inject constructor(val productWithCategoryDao: ProductWithCategoryDao) :
    ProductWithCategoryRepository {

    private fun productWithCategoryEntityToProductWithCategory(
        productWithCategoryEntity:
        ProductWithCategoryEntity
    ): ProductWithCategory {

        return ProductWithCategory(
            productWithCategoryEntity.id,
            productWithCategoryEntity.name,
            productWithCategoryEntity.price,
            productWithCategoryEntity.categoryId,
            productWithCategoryEntity.categoryName
        )
    }


    override suspend fun getAll(): Flow<Result<List<ProductWithCategory>>> {
        return flow {
            try {
                productWithCategoryDao.getAll().collect { productWithCategoryEntityList ->
                    val list = productWithCategoryEntityList.map { productWithCategoryEntity ->
                        productWithCategoryEntityToProductWithCategory(productWithCategoryEntity)
                    }
                    emit(Result.success(list))
                }

            } catch (e: SQLiteException) {
                emit(Result.failure(e))
            }

        }
    }

    override suspend fun getById(id: Int): Result<ProductWithCategory?> {
        return try {
            val data = productWithCategoryDao.getById(id)
            if (data != null) {
                Result.success(productWithCategoryEntityToProductWithCategory(data))
            } else {
                Result.success(null)
            }

        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    override suspend fun getSize(): Result<Int> {
        return try {
            Result.success(productWithCategoryDao.getSize())
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

}