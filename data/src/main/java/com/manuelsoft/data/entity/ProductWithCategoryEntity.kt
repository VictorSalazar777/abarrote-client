package com.manuelsoft.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products_with_category",
    indices = [Index(value = ["name"], unique = true)]
)
data class ProductWithCategoryEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("price")
    val price: Float,

    @ColumnInfo("category_id")
    val categoryId: Int,

    @ColumnInfo("category_name")
    val categoryName: String
)