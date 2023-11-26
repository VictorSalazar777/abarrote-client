package com.manuelsoft.app

import android.view.View
import com.manuelsoft.domain_model.model.ProductWithCategory

interface BtnUpdateClickInterface {
    fun onUpdateClick(product: ProductWithCategory, view: View? = null)
}

interface BtnDeleteClickInterface {
    fun onDeleteClick(product: ProductWithCategory, view: View? = null)
}