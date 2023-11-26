package com.manuelsoft.mylibrary

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(

    val id: Int,

    val name: String

)