package com.manuelsoft.data

import android.database.sqlite.SQLiteException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    @Throws(SQLiteException::class)
    fun getAll(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id=:id")
    @Throws(SQLiteException::class)
    suspend fun getById(id: Int): Product?

    @Query("SELECT * FROM products WHERE name=:name")
    @Throws(SQLiteException::class)
    suspend fun getByName(name: String): Product?

    @Insert
    @Throws(SQLiteException::class)
    suspend fun insert(product: Product)

    @Insert
    @Throws(SQLiteException::class)
    suspend fun insert(products: List<Product>)

    @Query("DELETE FROM products")
    @Throws(SQLiteException::class)
    suspend fun deleteAll(): Int

    @Query("SELECT COUNT(*) FROM products")
    @Throws(SQLiteException::class)
    suspend fun getSize(): Int


}