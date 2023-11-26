package com.manuelsoft.domain_usecases.usecases

import com.manuelsoft.domain_model.model.Product
import com.manuelsoft.domain_model.model.ProductWithCategory
import com.manuelsoft.domain_usecases.CrudResult
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CrudProductsUseCase {

    val resultFlow: SharedFlow<CrudResult<Unit>>
    val productFlow: SharedFlow<CrudResult<ProductWithCategory?>>
    val productWithCategoryListFlow: StateFlow<CrudResult<List<ProductWithCategory>>>

    suspend fun queryAllProducts()

    suspend fun addProduct(product: Product)

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(product: Product)

    suspend fun queryProductById(id: Int)

}