package com.manuelsoft.repository

data class Product (
    val id: Int,
    val name: String,
    val price: Float
) {
    constructor(
        name: String,
        price: Float) : this(0, name, price)
}