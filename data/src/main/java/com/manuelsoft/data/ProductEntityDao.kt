package com.manuelsoft.data

import android.database.sqlite.SQLiteException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductEntityDao {

    @Query("SELECT * FROM products")
    @Throws(SQLiteException::class)
    fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id=:id")
    @Throws(SQLiteException::class)
    suspend fun getById(id: Int): ProductEntity?

    @Query("SELECT * FROM products WHERE name=:name")
    @Throws(SQLiteException::class)
    suspend fun getByName(name: String): ProductEntity?

    @Insert
    @Throws(SQLiteException::class)
    suspend fun insert(productEntity: ProductEntity)

    @Insert
    @Throws(SQLiteException::class)
    suspend fun insert(productEntities: List<ProductEntity>)

    @Query("DELETE FROM products")
    @Throws(SQLiteException::class)
    suspend fun deleteAll(): Int

    @Query("SELECT COUNT(*) FROM products")
    @Throws(SQLiteException::class)
    suspend fun getSize(): Int


}