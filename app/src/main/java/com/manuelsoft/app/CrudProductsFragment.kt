package com.manuelsoft.app

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manuelsoft.app.databinding.CrudProductsBinding
import com.manuelsoft.repository.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrudProductsFragment : BaseFragment<CrudProductsBinding>() {

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> CrudProductsBinding
        get() = CrudProductsBinding::inflate

    private val crudProductsViewModel: CrudProductsViewModel by viewModels()

    private lateinit var adapter: ProductsRecyclerViewAdapter
    private var priceOk = false
    private var nameOk = false

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
                crudProductsViewModel.productListFlow().collectLatest { productList ->
                    adapter.setData(productList)
                }
            }
        }
    }

    private fun setupProductNameBarEditText() {
        binding.etProductname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
                verifyProductName(s)
                binding.btnAdd.isEnabled = nameOk && priceOk
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })
    }

    private fun verifyProductName(name: CharSequence?) {
        nameOk = !name.isNullOrBlank()
    }

    private fun setupProductPriceEditText() {
        binding.etProductprice.filters = arrayOf<InputFilter>(MoneyValueFilter())
        binding.etProductprice.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    //
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    verifyProductPrice(s)
                    binding.btnAdd.isEnabled = nameOk && priceOk
                }

                override fun afterTextChanged(s: Editable?) {
                    //
                }

            }

        )
    }

    private fun verifyProductPrice(price: CharSequence?) {
        priceOk = price.toString().toDoubleOrNull() != null
    }

    private fun setupAddButton() {
        binding.btnAdd.setOnClickListener { it ->
            crudProductsViewModel.addProduct(
                Product(
                    0,
                    binding.etProductname.text.toString(),
                    binding.etProductprice.text.toString().toFloat()
                )
            )
            binding.etProductname.text.clear()
            binding.etProductprice.text.clear()
            it.isEnabled = false
            nameOk = false
            priceOk = false
            Toast.makeText(requireContext(), "Producto agregado", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductsList()
        setupProductNameBarEditText()
        setupProductPriceEditText()
        setupAddButton()
        crudProductsViewModel.productListFlow()
    }

}