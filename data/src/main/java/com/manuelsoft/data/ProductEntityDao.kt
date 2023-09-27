package com.manuelsoft.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductEntityDao {

    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id=:id")
    suspend fun getById(id: Int): ProductEntity?

    @Query("SELECT * FROM products WHERE name=:name")
    suspend fun getByName(name: String): ProductEntity?

    @Insert
    suspend fun insert(productEntity: ProductEntity): Long

    @Insert
    suspend fun insert(productEntities: List<ProductEntity>): LongArray

    @Query("UPDATE products SET name=:name WHERE id=:id")
    suspend fun updateName(id: Int, name: String): Int

    @Query("UPDATE products SET price=:price WHERE id=:id")
    suspend fun updatePrice(id: Int, price: Float): Int

    @Update
    suspend fun updateProduct(productEntity: ProductEntity): Int

    @Query("DELETE FROM products WHERE id=:id")
    suspend fun deleteById(id: Int): Int

    @Delete
    suspend fun deleteList(productEntities: List<ProductEntity>): Int

    @Query("DELETE FROM products")
    suspend fun deleteAll(): Int

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getSize(): Int


}