package com.manuelsoft.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manuelsoft.domain_usecases.usecases.CrudCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudCategoriesViewModel @Inject constructor(private val useCase: CrudCategoriesUseCase) :
    ViewModel() {

    val listCategoriesFlow = useCase.listCategoriesFlow

    fun queryCategories() {
        viewModelScope.launch {
            useCase.queryCategories()
        }
    }

}