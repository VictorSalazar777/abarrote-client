package com.manuelsoft.app

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.manuelsoft.app.databinding.DialogUpdateBinding
import com.manuelsoft.repository.Product

class UpdateDialogFragment : DialogFragment() {

    private val crudProductsViewModel: CrudProductsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogUpdateBinding.inflate(inflater, container, false)

        val id = arguments?.getInt("id")
        val name = arguments?.getString("name")
        val price = arguments?.getFloat("price")

        val product = id?.let {
            if (name != null) {
                if (price != null) {
                    Product(it, name, price)
                }
            }
        } ?: throw RuntimeException("Valor nulo en argumento de dialogo")

        binding.etUpdateName.setText(name)
        binding.etUpdatePrice.filters = arrayOf<InputFilter>(MoneyValueFilter())
        binding.etUpdatePrice.setText(price.toString())

        binding.btnDialogUpdate.setOnClickListener {

            val updatedProduct = Product(
                id,
                binding.etUpdateName.text.toString(),
                binding.etUpdatePrice.text.toString().toFloat()
            )

            crudProductsViewModel.updateProduct(updatedProduct)

            dismiss()
        }


        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

    }
}
