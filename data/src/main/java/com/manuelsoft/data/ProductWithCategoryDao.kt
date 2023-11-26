package com.manuelsoft.data

import androidx.room.Dao
import androidx.room.Query
import com.manuelsoft.data.entity.ProductWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductWithCategoryDao {

    @Query("SELECT * FROM products_with_category")
    fun getAll(): Flow<List<ProductWithCategoryEntity>>

    @Query("SELECT * FROM products_with_category WHERE id=:id")
    suspend fun getById(id: Int): ProductWithCategoryEntity?

    @Query(
        "INSERT INTO products_with_category SELECT products.id, products.name, products.price, categories.id AS category_id, " +
                "categories.name AS category_name FROM products INNER JOIN" +
                " categories ON products.category_id = categories.id"
    )
    suspend fun insert()

    @Query("DELETE FROM products_with_category")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM products_with_category")
    suspend fun getSize(): Int

}
