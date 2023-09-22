package com.manuelsoft.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 1, exportSchema = true
)
abstract class RoomDb: RoomDatabase() {

    abstract fun productDao(): ProductEntityDao
}