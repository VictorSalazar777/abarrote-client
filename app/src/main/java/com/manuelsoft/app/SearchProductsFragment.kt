package com.manuelsoft.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.manuelsoft.app.databinding.SearchProductsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchProductsFragment() :
    BaseFragment<SearchProductsBinding>() {

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> SearchProductsBinding
        get() = SearchProductsBinding::inflate


    private val searchProductsViewModel: SearchProductsViewModel by viewModels()

    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val products = mutableListOf<String>()
        listAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, products)
        listAdapter.setNotifyOnChange(true)

    }

    private fun setupProductsList() {
        binding.productsList.adapter = listAdapter
        observeProductListSource()
    }

    private fun observeProductListSource() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                searchProductsViewModel.productsFlow.collectLatest { productList ->
                    //listAdapter.clear()
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
                listAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMenu()
        setupProductsList()
        setupSearchBar()
        searchProductsViewModel.getAllProducts()
    }

    private fun setMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return menuItem.onNavDestinationSelected(findNavController())
            }

        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    override fun onResume() {
        super.onResume()
    }


}