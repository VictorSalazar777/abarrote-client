package com.manuelsoft.app.showProducts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manuelsoft.app.BaseFragment
import com.manuelsoft.app.CrudProductsViewModel
import com.manuelsoft.app.R
import com.manuelsoft.app.databinding.SearchProductsFragmentBinding
import com.manuelsoft.app.handleErrorCrudResult
import com.manuelsoft.domain_model.model.ProductWithCategory
import com.manuelsoft.domain_usecases.CrudResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowProductsFragment :
    BaseFragment<SearchProductsFragmentBinding>() {

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> SearchProductsFragmentBinding
        get() = SearchProductsFragmentBinding::inflate


    private val crudProductsViewModel: CrudProductsViewModel by activityViewModels()

    private lateinit var adapter: ShowProductsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ShowProductsRecyclerViewAdapter()

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

    private fun setupUpdateButton() {
        binding.btnUpdateContent.setOnClickListener {
            crudProductsViewModel.update()
        }
    }

    private fun setupSearchBar() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        }

        val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                binding.etSearchbar.addTextChangedListener(textWatcher)
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                binding.etSearchbar.removeTextChangedListener(textWatcher)
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMenu()
        setupUpdateButton()
        setupProductsList()
        setupSearchBar()
        crudProductsViewModel.queryProductsWithCategories()
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


}