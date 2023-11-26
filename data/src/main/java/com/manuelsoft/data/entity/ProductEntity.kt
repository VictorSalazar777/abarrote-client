package com.manuelsoft.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [Index(value = ["name"], unique = true)]
)
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo(name = "category_id")
    val categoryId: Int,

    @ColumnInfo("price")
    val price: Float
)