package com.manuelsoft.app

import android.view.View
import com.manuelsoft.repository.Product

interface BtnUpdateClickInterface {
    fun onUpdateClick(product: Product, view: View? = null)
}

interface BtnDeleteClickInterface {
    fun onDeleteClick(product: Product, view: View? = null)
}