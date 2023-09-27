package com.manuelsoft.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuelsoft.repository.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudProductsViewModel @Inject constructor(private val crudProductsUseCase: CrudProductsUseCase) :
    ViewModel() {

    private val _resultFlow: MutableSharedFlow<Result<String>> = MutableSharedFlow(replay = 0)
    val resultFlow: SharedFlow<Result<String>> get() = _resultFlow

    fun productListFlow(): Flow<List<Product>> {
        return crudProductsUseCase.productListFlow()
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            _resultFlow.emit(crudProductsUseCase.addProduct(product))
        }
    }

    fun updateProductName(product: Product) {
        viewModelScope.launch {
            _resultFlow.emit(crudProductsUseCase.updateName(product.id, product.name))
        }
    }

    fun updateProductPrice(product: Product) {
        viewModelScope.launch {
            _resultFlow.emit(crudProductsUseCase.updatePrice(product.id, product.price))
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            _resultFlow.emit(crudProductsUseCase.updateProduct(product))
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            _resultFlow.emit(crudProductsUseCase.deleteProduct(product))
        }
    }


}