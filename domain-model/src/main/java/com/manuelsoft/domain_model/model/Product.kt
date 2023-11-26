package com.manuelsoft.domain_model.model

data class Product(
    val id: Int,
    val name: String,
    val categoryId: Int,
    val price: Float
)