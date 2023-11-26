package com.manuelsoft.domain_model.model

data class ProductWithCategory(
    val id: Int,
    val name: String,
    val price: Float,
    val categoryId: Int,
    val categoryName: String
)