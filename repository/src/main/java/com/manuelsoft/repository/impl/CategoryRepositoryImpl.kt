package com.manuelsoft.repository.impl

import android.database.sqlite.SQLiteException
import com.manuelsoft.data.CategoryEntityDao
import com.manuelsoft.data.entity.CategoryEntity
import com.manuelsoft.domain_model.model.Category
import com.manuelsoft.domain_repository.repository.CategoryRepository
import com.manuelsoft.mylibrary.AbarroteCategoryService
import com.manuelsoft.mylibrary.CategoryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class CategoryRepositoryImpl @Inject constructor(
    private val categoryEntityDao: CategoryEntityDao,
    private val abarroteCategoryService: AbarroteCategoryService
) : CategoryRepository {


    private fun categoryDtoToCategoryEntity(categoryDto: CategoryDto): CategoryEntity {
        return CategoryEntity(categoryDto.id, categoryDto.name)
    }

    private fun categoryToCategoryDto(category: Category): CategoryDto {
        return CategoryDto(category.id, category.name)
    }

    private fun categoryEntityToCategory(categoryEntity: CategoryEntity): Category {
        return Category(categoryEntity.id, categoryEntity.name)
    }

    private fun categoryToCategoryEntity(category: Category): CategoryEntity {
        return CategoryEntity(category.id, category.name)
    }

    override fun getAll(): Flow<Result<List<Category>>> {
        return flow {
            try {
                categoryEntityDao.getAll().collect { categoriesEntityList ->
                    val categoriesList = categoriesEntityList.map { categoryEntity ->
                        categoryEntityToCategory(categoryEntity)
                    }
                    emit(Result.success(categoriesList))
                }

            } catch (e: SQLiteException) {
                emit(Result.failure(e))
            }
        }
    }

    override suspend fun getById(id: Int): Result<Category?> {
        return try {
            val categoryEntity = categoryEntityDao.getById(id)
            if (categoryEntity != null) {
                val category = categoryEntityToCategory(categoryEntity)
                Result.success(category)
            } else {
                Result.success(null)
            }
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    override suspend fun add(category: Category): Result<Unit> {

        val result = abarroteCategoryService.createCategory(categoryToCategoryDto(category))

        return if (result.isSuccess) {
            val categoryDto = result.getOrNull()!!
            try {
                categoryEntityDao.addCategory(categoryDtoToCategoryEntity(categoryDto))
                Result.success(Unit)
            } catch (e: SQLiteException) {
                Result.failure(e)
            }

        } else {
            Result.failure(result.exceptionOrNull()!!)
        }

    }


    override suspend fun updateCategory(category: Category): Result<Unit> {
        val categoryDto = categoryToCategoryDto(category)
        val result = abarroteCategoryService.updateCategory(categoryDto.id, categoryDto)

        return if (result.isSuccess) {
            try {
                categoryEntityDao.updateCategory(categoryDtoToCategoryEntity(result.getOrNull()!!))
                Result.success(Unit)
            } catch (e: SQLiteException) {
                Result.failure(e)
            }
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }


    }

    override suspend fun deleteById(id: Int): Result<Unit> {
        val result = abarroteCategoryService.deleteCategory(id)

        return if (result.isSuccess) {
            try {
                categoryEntityDao.deleteById(id)
                Result.success(Unit)
            } catch (e: SQLiteException) {
                Result.failure(e)
            }
        } else {
            Result.failure(result.exceptionOrNull()!!)
        }
    }

    override suspend fun getSize(): Result<Int> {
        return try {
            Result.success(categoryEntityDao.getSize())
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }


}