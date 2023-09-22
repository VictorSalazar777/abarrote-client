package com.manuelsoft.repository

import android.database.sqlite.SQLiteFullException
import com.manuelsoft.data.ProductEntity
import com.manuelsoft.data.ProductEntityDao

class FakeProductEntityDao(private val failure: Boolean): ProductEntityDao {

    private val prods = mutableListOf <ProductEntity>()

    private fun throwExceptionIfNeeded() {
        if (failure) {
            throw SQLiteFullException()
        }
    }

    override suspend fun getAll(): List<ProductEntity> {
        throwExceptionIfNeeded()
        return prods
    }

    override suspend fun getById(id: Int): ProductEntity? {
        throwExceptionIfNeeded()
        return prods.find { it.id == id }
    }

    override suspend fun getByName(name: String): ProductEntity? {
        throwExceptionIfNeeded()
        return prods.find { it.name == name }
    }

    override suspend fun insert(productEntity: ProductEntity) {
        throwExceptionIfNeeded()
        val fakeId = prods.size + 1
        prods.add(ProductEntity(fakeId, productEntity.name, productEntity.price))
    }

    override suspend fun insert(productEntities: List<ProductEntity>) {
        throwExceptionIfNeeded()
        productEntities.forEach { productEntity ->
            val prod = productEntity.copy(id = prods.size + 1)
            prods.add(prod)
        }
    }

    override suspend fun deleteById(id: Int) {
        throwExceptionIfNeeded()
        prods.removeAll{ it.id == id }
    }

    override suspend fun deleteList(productEntities: List<ProductEntity>) {
        throwExceptionIfNeeded()
        prods.removeAll { prod ->
            productEntities.any { it.id == prod.id }
        }
    }

    override suspend fun deleteAll() {
        throwExceptionIfNeeded()
        prods.clear()
    }

    override suspend fun getSize(): Int {
        throwExceptionIfNeeded()
        return prods.size
    }
}