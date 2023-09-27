package com.manuelsoft.app

import android.database.sqlite.SQLiteException
import com.manuelsoft.repository.Product
import com.manuelsoft.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CrudProductsUseCase @Inject constructor(private val repository: Repository) {

    val fail = "No se pudo actualizar: item no encontrado"
    val addOk = "Producto agregado"
    val updateOk = "Producto actualizado"
    val deleteOk = "Producto eliminado"

    fun productListFlow(): Flow<List<Product>> {
        return repository.getAll()
    }

    suspend fun addProduct(product: Product): Result<String> {
        return try {
            repository.add(product)
            Result.success(addOk)
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    suspend fun updateName(id: Int, name: String): Result<String> {
        return try {
            val updateProduct = repository.updateName(id, name)
            if (updateProduct == 1) {
                Result.success(updateOk)
            } else {
                Result.failure(RuntimeException(fail))
            }
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    suspend fun updatePrice(id: Int, price: Float): Result<String> {
        return try {
            val updateProduct = repository.updatePrice(id, price)
            if (updateProduct == 1) {
                Result.success(updateOk)
            } else {
                Result.failure(RuntimeException(fail))
            }
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(product: Product): Result<String> {
        return try {
            val updateProduct = repository.updateProduct(product)
            if (updateProduct == 1) {
                Result.success(updateOk)
            } else {
                Result.failure(RuntimeException(fail))
            }
        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(product: Product): Result<String> {
        return try {
            val deleteProduct = repository.deleteById(product.id)
            if (deleteProduct == 1) {
                Result.success(deleteOk)
            } else {
                Result.failure(RuntimeException(fail))
            }

        } catch (e: SQLiteException) {
            Result.failure(e)
        }
    }

}