package com.manuelsoft.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.manuelsoft.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryEntityDao {

    @Query("SELECT * FROM categories")
    fun getAll(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id=:id")
    suspend fun getById(id: Int): CategoryEntity?

    @Upsert
    suspend fun addCategory(category: CategoryEntity)

    @Upsert
    suspend fun addCategories(categories: List<CategoryEntity>)

    @Update
    suspend fun updateCategory(category: CategoryEntity): Int

    @Query("DELETE FROM categories WHERE id=:id")
    suspend fun deleteById(id: Int): Int

    @Query("DELETE FROM categories")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getSize(): Int

}