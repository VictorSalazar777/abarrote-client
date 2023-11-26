package com.manuelsoft.app.manageProducts

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manuelsoft.app.BaseFragment
import com.manuelsoft.app.BtnDeleteClickInterface
import com.manuelsoft.app.BtnUpdateClickInterface
import com.manuelsoft.app.Categories
import com.manuelsoft.app.CategoriesAndProduct
import com.manuelsoft.app.CrudCategoriesViewModel
import com.manuelsoft.app.CrudProductsViewModel
import com.manuelsoft.app.MoneyValueFilter
import com.manuelsoft.app.databinding.ManageProductsFragmentBinding
import com.manuelsoft.app.handleErrorCrudResult
import com.manuelsoft.app.handleSuccessCrudResult
import com.manuelsoft.app.mapCategoryToCategoryP
import com.manuelsoft.app.mapProductWithCategoryToProductWithCategoryP
import com.manuelsoft.domain_model.model.Category
import com.manuelsoft.domain_model.model.ProductWithCategory
import com.manuelsoft.domain_usecases.CrudResult
import com.manuelsoft.domain_usecases.mapProductWithCategoryToProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageProductsFragment : BaseFragment<ManageProductsFragmentBinding>(),
    BtnUpdateClickInterface,
    BtnDeleteClickInterface {

    private val TAG = ManageProductsFragment::class.java.name

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> ManageProductsFragmentBinding
        get() = ManageProductsFragmentBinding::inflate

    private val crudProductsViewModel: CrudProductsViewModel by activityViewModels()
    private val crudCategoriesViewModel: CrudCategoriesViewModel by activityViewModels()

    private lateinit var adapter: ManageProductsRecyclerViewAdapter
    private lateinit var categoriesResult: CrudResult<List<Category>>
    private var etProductNameCurrentText: CharSequence? = null
    private var priceOk = false
    private var nameOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ManageProductsRecyclerViewAdapter(this, this)

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
                crudProductsViewModel.productWithCategoryListFlow.collectLatest { productListResult ->
                    if (productListResult is CrudResult.Success<List<ProductWithCategory>>) {
                        val productList = productListResult.content
                        adapter.setData(productList)

                    } else {
                        handleErrorCrudResult(requireActivity(), productListResult)
                    }

                }
            }
        }
    }

    private fun observeResultFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                crudProductsViewModel.resultFlow.collectLatest { crudResult ->

                    if (crudResult is CrudResult.Success<Unit>) {
                        handleSuccessCrudResult(requireActivity(), crudResult)

                    } else {
                        handleErrorCrudResult(requireActivity(), crudResult)
                    }
                }
            }
        }

    }


    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                crudCategoriesViewModel.listCategoriesFlow.collectLatest {
                    categoriesResult = it
                    Log.d(TAG, it.toString())
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
        binding.btnAdd.setOnClickListener {
            if (categoriesResult is CrudResult.Success) {
                val categories = (categoriesResult as CrudResult.Success<List<Category>>).content
                val categoriesP = categories.map { category ->
                    mapCategoryToCategoryP(category)
                }
                val action =
                    ManageProductsFragmentDirections.actionCrudProductsFragmentToAddProductDialogFragment(
                        Categories(categoriesP)
                    )

                findNavController().navigate(action)
            } else {
                handleErrorCrudResult(requireActivity(), categoriesResult)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProductsList()
        setupProductNameBarEditText()
        setupProductPriceEditText()
        setupAddButton()
        crudCategoriesViewModel.queryCategories()
        crudProductsViewModel.queryProductsWithCategories()
        observeResultFlow()
        observeCategories()
    }

    override fun onUpdateClick(product: ProductWithCategory, view: View?) {

        if (categoriesResult is CrudResult.Success) {
            val categories = (categoriesResult as CrudResult.Success<List<Category>>).content

            val categoriesP = categories.map { category ->
                mapCategoryToCategoryP(category)
            }

            val productP = mapProductWithCategoryToProductWithCategoryP(product)

            val data = CategoriesAndProduct(categoriesP, productP)
            val action =
                ManageProductsFragmentDirections.actionCrudProductsFragmentToUpdateDialogFragment(
                    data
                )

            findNavController().navigate(action)
        } else {
            handleErrorCrudResult(requireActivity(), categoriesResult)
        }

    }

    override fun onDeleteClick(productWithCategory: ProductWithCategory, view: View?) {
        val product = mapProductWithCategoryToProduct(productWithCategory)
        crudProductsViewModel.deleteProduct(product)
        binding.etProductname.text.clear()
        binding.etProductprice.text.clear()
    }


}