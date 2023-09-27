package com.manuelsoft.app

import android.view.View
import com.manuelsoft.repository.Product

interface BtnUpdateClickInterface {
    fun onUpdateClick(view: View, product: Product)
}

interface BtnDeleteClickInterface {
    fun onDeleteClick(view: View, product: Product)
}