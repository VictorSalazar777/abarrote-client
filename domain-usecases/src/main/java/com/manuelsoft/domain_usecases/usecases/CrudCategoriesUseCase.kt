package com.manuelsoft.domain_usecases.usecases

import com.manuelsoft.domain_model.model.Category
import com.manuelsoft.domain_usecases.CrudResult
import kotlinx.coroutines.flow.StateFlow

interface CrudCategoriesUseCase {

    val listCategoriesFlow: StateFlow<CrudResult<List<Category>>>

    suspend fun queryCategories()
}