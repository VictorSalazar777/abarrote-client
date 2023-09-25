package com.manuelsoft.app

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manuelsoft.app.databinding.CrudProductsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrudProductsFragment : BaseFragment<CrudProductsBinding>() {

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> CrudProductsBinding
        get() = CrudProductsBinding::inflate

    private val crudProductsViewModel: CrudProductsViewModel by viewModels()

    private lateinit var adapter: ProductsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ProductsRecyclerViewAdapter()

    }

    private fun setupProductsList() {
        binding.productsList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.productsList.adapter = adapter
        observeProductListSource()
    }

    private fun observeProductListSource() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                crudProductsViewModel.productsFlow.collectLatest { productList ->
                    adapter.setData(productList)
                }
            }
        }
    }

    private fun setupProductNameBar() {
        binding.etProductname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })
    }

    private fun setupProductPrice() {
        binding.etProductprice.filters = arrayOf<InputFilter>(MoneyValueFilter())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductsList()
        setupProductNameBar()
        setupProductPrice()
        crudProductsViewModel.getAllProducts()
    }

}