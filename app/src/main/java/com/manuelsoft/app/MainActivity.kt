package com.manuelsoft.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.manuelsoft.app.databinding.MainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    private lateinit var binding: MainBinding
    private lateinit var listAdapter: ArrayAdapter<String>
    private val productsViewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupProductsList()
        setupSearchBar()
        productsViewModel.getAllProducts()
    }

    private fun setupProductsList() {
        val products = mutableListOf<String>()
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, products)
        binding.productsList.adapter = listAdapter
        observeProductListSource()
    }

    private fun observeProductListSource() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                productsViewModel.productsFlow.collectLatest { productList ->
                    listAdapter.clear()
                    listAdapter.addAll(productList.map { product ->
                        product.name
                    })
                }
            }
        }
    }


    private fun setupSearchBar() {
        binding.etSearchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listAdapter.filter.filter(s);
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })
    }

}