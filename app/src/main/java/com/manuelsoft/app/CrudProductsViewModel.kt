package com.manuelsoft.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuelsoft.domain_model.model.Product
import com.manuelsoft.domain_usecases.usecases.CrudProductsUseCase
import com.manuelsoft.domain_usecases.usecases.UpdaterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudProductsViewModel @Inject constructor(
    private val crudProductsUseCase: CrudProductsUseCase, private val updaterUseCase: UpdaterUseCase
) :
    ViewModel() {

    val resultFlow = crudProductsUseCase.resultFlow
    val productFlow = crudProductsUseCase.productFlow
    val productWithCategoryListFlow = crudProductsUseCase.productWithCategoryListFlow


    fun update() {
        viewModelScope.launch {
            updaterUseCase.update()
        }
    }

    fun queryProductsWithCategories() {
        viewModelScope.launch {
            crudProductsUseCase.queryAllProducts()
        }
    }

    fun queryProductById(id: Int) {
        viewModelScope.launch {
            crudProductsUseCase.queryProductById(id)
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            crudProductsUseCase.addProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            crudProductsUseCase.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            crudProductsUseCase.deleteProduct(product)
        }
    }


}