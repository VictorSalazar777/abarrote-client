package com.manuelsoft.app

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manuelsoft.app.databinding.CrudProductsBinding
import com.manuelsoft.repository.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CrudProductsFragment : BaseFragment<CrudProductsBinding>(), BtnUpdateClickInterface,
    BtnDeleteClickInterface {

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> CrudProductsBinding
        get() = CrudProductsBinding::inflate

    private val crudProductsViewModel: CrudProductsViewModel by activityViewModels()

    private lateinit var adapter: ProductsRecyclerViewAdapter
    private var etProductNameCurrentText: CharSequence? = null
    private var priceOk = false
    private var nameOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ProductsRecyclerViewAdapter(this, this)

    }

    private fun setupProductsList() {
        binding.productsList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.productsList.adapter = adapter
        observeProductListSource()
    }

    private fun observeProductListSource() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                crudProductsViewModel.productListFlow().collectLatest { productList ->
                    adapter.setData(productList)
                }
            }
        }
    }

    private fun observeResultFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                crudProductsViewModel.resultFlow.collectLatest {

                    if (it.isSuccess) {
                        val value = it.getOrNull()
                        Toast.makeText(requireContext(), value, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val value = it.exceptionOrNull()
                        if (value != null) {
                            Toast.makeText(requireContext(), value.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
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
                etProductNameCurrentText = s
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

        val textWatcher = object : TextWatcher {
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

        val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                binding.etProductprice.addTextChangedListener(textWatcher)
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                binding.etProductprice.removeTextChangedListener(textWatcher)
            }
        }
        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
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
        observeResultFlow()
    }

    override fun onUpdateClick(view: View, product: Product) {

        val bundle = bundleOf(
            "id" to product.id,
            "name" to product.name,
            "price" to product.price
        )

        findNavController().navigate(
            R.id.action_crudProductsFragment_to_updateDialogFragment,
            bundle
        )
    }

    override fun onDeleteClick(view: View, product: Product) {
        crudProductsViewModel.deleteProduct(product)
        binding.etProductname.text.clear()
        binding.etProductprice.text.clear()
    }


}