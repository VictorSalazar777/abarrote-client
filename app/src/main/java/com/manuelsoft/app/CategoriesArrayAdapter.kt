package com.manuelsoft.app

import android.content.Context
import android.widget.ArrayAdapter
import com.manuelsoft.domain_model.model.Category

class CategoriesArrayAdapter(
    context: Context,
    resource: Int,
    private val categories: List<Category>
) :
    ArrayAdapter<String>(context, resource, categories.map { category -> category.name }) {

    override fun getItemId(position: Int): Long {
        return categories[position].id.toLong()
    }

    override fun getItem(position: Int): String {
        return categories[position].name
    }

}