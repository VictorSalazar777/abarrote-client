package com.manuelsoft.mylibrary

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDto(

    val id: Int,

    val name: String,

    val categoryId: Int,

    val price: Float
)