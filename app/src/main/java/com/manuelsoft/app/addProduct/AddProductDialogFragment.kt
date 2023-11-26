package com.manuelsoft.app.addProduct

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.manuelsoft.app.CategoriesArrayAdapter
import com.manuelsoft.app.CrudProductsViewModel
import com.manuelsoft.app.MoneyValueFilter
import com.manuelsoft.app.R
import com.manuelsoft.app.databinding.ProductDialogAddBinding
import com.manuelsoft.app.mapCategoryPToCategory
import com.manuelsoft.domain_model.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductDialogFragment : DialogFragment() {

    private var _binding: ProductDialogAddBinding? = null
    private val binding get() = _binding!!
    private var selectedCategoryId = 0

    private val crudProductsViewModel: CrudProductsViewModel by activityViewModels()
    private val args: AddProductDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductDialogAddBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = args.data.categories.map { categoryP ->
            mapCategoryPToCategory(categoryP)
        }

        binding.etProductPrice.filters = arrayOf<InputFilter>(MoneyValueFilter())
        val adapter =
            CategoriesArrayAdapter(requireActivity(), R.layout.category_list_item, categories)

        binding.actvCategories.setAdapter(adapter)
        binding.actvCategories.setText("Seleccione", false)
        binding.actvCategories.setOnItemClickListener { _, _, _, id ->
            selectedCategoryId = id.toInt()

        }
        binding.btnDialogAdd.setOnClickListener {
            val name = binding.etProductName.text.toString()
            val price = binding.etProductPrice.text.toString().toFloat()
            val product = Product(0, name, selectedCategoryId, price)
            crudProductsViewModel.updateProduct(product)
            dismiss()
        }
        binding.btnDialogCancel.setOnClickListener {
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

