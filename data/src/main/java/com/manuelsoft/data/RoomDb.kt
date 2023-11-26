package com.manuelsoft.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.manuelsoft.data.entity.CategoryEntity
import com.manuelsoft.data.entity.ProductEntity
import com.manuelsoft.data.entity.ProductWithCategoryEntity

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        ProductWithCategoryEntity::class
    ],
    version = 1, exportSchema = true
)
abstract class RoomDb : RoomDatabase() {

    abstract fun productDao(): ProductEntityDao

    abstract fun categoryDao(): CategoryEntityDao

    abstract fun productWithCategoryDao(): ProductWithCategoryDao
}