package com.manuelsoft.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuelsoft.repository.Product
import com.manuelsoft.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudProductsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun productListFlow(): Flow<List<Product>> {
        return repository.getAll()
    }


    fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.add(product)
        }
    }

    fun updateProduct(product: Product) {


    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteById(product.id)
        }
    }


}