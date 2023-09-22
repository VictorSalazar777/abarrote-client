package com.manuelsoft.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductEntityDao {

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id=:id")
    suspend fun getById(id: Int): ProductEntity?

    @Query("SELECT * FROM products WHERE name=:name")
    suspend fun getByName(name: String): ProductEntity?

    @Insert
    suspend fun insert(productEntity: ProductEntity)

    @Insert
    suspend fun insert(productEntities: List<ProductEntity>)

    @Query("DELETE FROM products WHERE id=:id")
    suspend fun deleteById(id: Int)

    @Delete
    suspend fun deleteList(productEntities: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getSize(): Int


}