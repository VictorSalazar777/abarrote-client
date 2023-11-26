package com.manuelsoft.domain_impl.usecase

import com.manuelsoft.domain_model.model.Category
import com.manuelsoft.domain_repository.repository.CategoryRepository
import com.manuelsoft.domain_usecases.CrudResult
import com.manuelsoft.domain_usecases.MessageType
import com.manuelsoft.domain_usecases.sendErrorMessage
import com.manuelsoft.domain_usecases.sendSuccessMessage
import com.manuelsoft.domain_usecases.usecases.CrudCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

internal class CrudCategoriesUseCaseImpl
@Inject constructor(private val repository: CategoryRepository) : CrudCategoriesUseCase {

    private val _categoriesFlow =
        MutableStateFlow<CrudResult<List<Category>>>(
            CrudResult.Success(
                MessageType.CATEGORY_QUERIED_SUCCESSFULLY,
                listOf()
            )
        )
    override val listCategoriesFlow = _categoriesFlow.asStateFlow()

    override suspend fun queryCategories() {
        repository.getAll().collectLatest { result ->
            if (result.isSuccess) {
                sendSuccessMessage(
                    _categoriesFlow,
                    MessageType.CATEGORY_QUERIED_SUCCESSFULLY,
                    result
                )
            } else {
                sendErrorMessage(
                    _categoriesFlow,
                    MessageType.CATEGORY_QUERY_ERROR,
                    result
                )
            }
        }


    }

}