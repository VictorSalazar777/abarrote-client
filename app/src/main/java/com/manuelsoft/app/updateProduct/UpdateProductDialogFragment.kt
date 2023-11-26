package com.manuelsoft.app.updateProduct

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
import com.manuelsoft.app.databinding.ProductDialogUpdateBinding
import com.manuelsoft.app.mapCategoryPToCategory
import com.manuelsoft.app.mapProductWithCategoryPToProductWithCategory
import com.manuelsoft.domain_model.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProductDialogFragment : DialogFragment() {

    private var _binding: ProductDialogUpdateBinding? = null
    private val binding get() = _binding!!
    private var selectedCategoryId = 0


    private val crudProductsViewModel: CrudProductsViewModel by activityViewModels()
    private val args: UpdateProductDialogFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductDialogUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productWithCategory =
            mapProductWithCategoryPToProductWithCategory(args.data.product)
        val categories = args.data.categories.map { categoryP ->
            mapCategoryPToCategory(categoryP)
        }

        binding.etUpdateName.setText(productWithCategory.name)
        binding.etUpdatePrice.filters = arrayOf<InputFilter>(MoneyValueFilter())
        binding.etUpdatePrice.setText(productWithCategory.price.toString())
        val adapter =
            CategoriesArrayAdapter(requireActivity(), R.layout.category_list_item, categories)


        binding.actvCategories.setAdapter(adapter)
        binding.actvCategories.setText(productWithCategory.categoryName, false)
        binding.actvCategories.setOnItemClickListener { _, _, _, id ->
            selectedCategoryId = id.toInt()

        }
        binding.btnDialogUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val price = binding.etUpdatePrice.text.toString().toFloat()
            val product = Product(productWithCategory.id, name, selectedCategoryId, price)
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
