package com.manuelsoft.domain_usecases

import com.manuelsoft.domain_model.model.Product
import com.manuelsoft.domain_model.model.ProductWithCategory
import kotlinx.coroutines.flow.MutableSharedFlow

enum class MessageType {
    PRODUCT_QUERIED_SUCCESSFULLY,
    PRODUCT_ADDED_SUCCESSFULLY,
    PRODUCT_REMOVED_SUCCESSFULLY,
    PRODUCT_UPDATED_SUCCESSFULLY,
    CATEGORY_QUERIED_SUCCESSFULLY,
    CATEGORY_ADDED_SUCCESSFULLY,
    CATEGORY_UPDATED_SUCCESSFULLY,
    CATEGORY_REMOVED_SUCCESSFULLY,
    PRODUCT_WITH_CATEGORIES_QUERIED_SUCCESSFULLY,
    PRODUCT_WITH_CATEGORIES_QUERY_ERROR,
    PRODUCT_QUERY_ERROR,
    PRODUCT_ADD_ERROR,
    PRODUCT_UPDATE_ERROR,
    PRODUCT_REMOVE_ERROR,
    CATEGORY_QUERY_ERROR,
    CATEGORY_ADD_ERROR,
    CATEGORY_UPDATE_ERROR,
    CATEGORY_REMOVE_ERROR,
}

sealed class CrudResult<out T> {
    class Success<T>(val messageType: MessageType, val content: T) : CrudResult<T>()

    class Failure(val messageType: MessageType, val message: String) : CrudResult<Nothing>()
}

fun mapProductWithCategoryToProduct(productWithCategory: ProductWithCategory): Product {
    return Product(
        productWithCategory.id,
        productWithCategory.name,
        productWithCategory.categoryId,
        productWithCategory.price
    )
}

suspend fun <T> sendSuccessMessage(
    flow: MutableSharedFlow<CrudResult<T>>,
    messageType: MessageType,
    result: Result<T>
) {
    flow.emit(CrudResult.Success(messageType, result.getOrNull()!!))
}

suspend fun <T> sendErrorMessage(
    flow: MutableSharedFlow<CrudResult<T>>,
    messageType: MessageType,
    result: Result<T>
) {
    val e = result.exceptionOrNull()
    var message = ""
    if (e != null) {
        if (e.message != null) {
            message = e.message!!
        }
    }

    flow.emit(CrudResult.Failure(messageType, message))
}