package com.manuelsoft.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuelsoft.repository.Product
import com.manuelsoft.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchProductsViewModel @Inject constructor(private val repository: Repository): ViewModel() {


    private val _productsFlow = MutableStateFlow<List<Product>>(mutableListOf())
    val productsFlow = _productsFlow.asStateFlow()

    fun getAllProducts() {
        viewModelScope.launch {

//            val prod = Product(0, "aceite", 6.0f)
//            val prod2 = Product(0, "arroz", 7.0f)
//            val prod3 = Product(0, "lejia", 4.0f)
//            val prod4 = Product(0, "leche", 5.0f)
//            val prods = listOf(prod, prod2, prod3, prod4)
//            repository.addList(prods)

            _productsFlow.value = repository.getAll()
        }
    }


}