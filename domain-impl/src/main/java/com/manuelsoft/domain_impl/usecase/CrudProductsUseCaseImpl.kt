package com.manuelsoft.domain_impl.usecase

import com.manuelsoft.domain_model.model.Product
import com.manuelsoft.domain_model.model.ProductWithCategory
import com.manuelsoft.domain_repository.repository.ProductRepository
import com.manuelsoft.domain_repository.repository.ProductWithCategoryRepository
import com.manuelsoft.domain_usecases.CrudResult
import com.manuelsoft.domain_usecases.MessageType
import com.manuelsoft.domain_usecases.sendErrorMessage
import com.manuelsoft.domain_usecases.sendSuccessMessage
import com.manuelsoft.domain_usecases.usecases.CrudProductsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


internal class CrudProductsUseCaseImpl @Inject constructor
    (
    private val productWithCategoryRepository: ProductWithCategoryRepository,
    private val productRepository: ProductRepository
) : CrudProductsUseCase {

    private val _resultFlow = MutableSharedFlow<CrudResult<Unit>>()
    override val resultFlow = _resultFlow.asSharedFlow()

    private val _productFlow = MutableSharedFlow<CrudResult<ProductWithCategory?>>()
    override val productFlow = _productFlow.asSharedFlow()

    private val _productListFlow =
        MutableStateFlow<CrudResult<List<ProductWithCategory>>>(
            CrudResult.Success(
                MessageType.PRODUCT_WITH_CATEGORIES_QUERIED_SUCCESSFULLY,
                listOf()
            )
        )

    override val productWithCategoryListFlow: StateFlow<CrudResult<List<ProductWithCategory>>> =
        _productListFlow.asStateFlow()

    override suspend fun queryAllProducts() {
        productWithCategoryRepository.getAll().collect { result ->
            if (result.isSuccess) {
                sendSuccessMessage(
                    _productListFlow,
                    MessageType.PRODUCT_QUERIED_SUCCESSFULLY,
                    result
                )
            } else {
                sendErrorMessage(
                    _productListFlow,
                    MessageType.PRODUCT_WITH_CATEGORIES_QUERY_ERROR,
                    result
                )
            }
        }
    }

    override suspend fun addProduct(product: Product) {
        val result = productRepository.add(product)

        if (result.isSuccess) {
            sendSuccessMessage(_resultFlow, MessageType.PRODUCT_QUERIED_SUCCESSFULLY, result)
        } else {
            sendErrorMessage(_resultFlow, MessageType.PRODUCT_ADD_ERROR, result)
        }
    }


    override suspend fun updateProduct(product: Product) {
        val result = productRepository.updateProduct(product)
        if (result.isSuccess) {
            sendSuccessMessage(_resultFlow, MessageType.CATEGORY_UPDATED_SUCCESSFULLY, result)
        } else {
            sendErrorMessage(
                _resultFlow,
                MessageType.PRODUCT_UPDATE_ERROR, result
            )
        }
    }

    override suspend fun deleteProduct(product: Product) {
        val result = productRepository.deleteById(product.id)
        if (result.isSuccess) {
            sendSuccessMessage(_resultFlow, MessageType.PRODUCT_REMOVED_SUCCESSFULLY, result)
        } else {
            sendErrorMessage(
                _resultFlow,
                MessageType.PRODUCT_REMOVE_ERROR, result
            )
        }
    }

    override suspend fun queryProductById(id: Int) {
        val result = productWithCategoryRepository.getById(id)
        if (result.isSuccess) {
            sendSuccessMessage(_productFlow, MessageType.PRODUCT_QUERIED_SUCCESSFULLY, result)
        } else {
            sendErrorMessage(_productFlow, MessageType.PRODUCT_QUERY_ERROR, result)
        }
    }


}