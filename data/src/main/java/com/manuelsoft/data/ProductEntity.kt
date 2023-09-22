package com.manuelsoft.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "products"
)
data class ProductEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val price: Float
) {
    constructor(
        name: String,
        price: Float) : this(0, name, price)
}