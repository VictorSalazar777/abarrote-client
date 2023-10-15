package com.manuelsoft.app

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.manuelsoft.app.databinding.ProductItemManageNarrowerBinding
import com.manuelsoft.app.databinding.ProductItemManageWiderBinding
import com.manuelsoft.repository.Product

class ManageProductsRecyclerViewAdapter(
    private val btnUpdateClick: BtnUpdateClickInterface,
    private val btnDeleteClick: BtnDeleteClickInterface
) : RecyclerView.Adapter<ViewHolder>(), Filterable {

    private var productList: List<Product> = listOf()
    private var productListFiltered: List<Product> = listOf()
    private var screenWidthInDp: Float = 0f

    class NarrowerViewHolder(
        private val binding: ProductItemManageNarrowerBinding,
        private val btnUpdateClick: BtnUpdateClickInterface,
        private val btnDeleteClick: BtnDeleteClickInterface
    ) : ViewHolder(binding.root) {

        private lateinit var myProduct: Product

        fun setData(product: Product) {
            myProduct = product
            binding.productName.text = product.name
            binding.productPrice.text = product.price.toString()
            binding.btnEdit.setOnClickListener {
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_manage_products_options, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        if (item?.itemId == R.id.update_option) {
                            btnUpdateClick.onUpdateClick(product)
                            return true
                        } else if (item?.itemId == R.id.delete_option) {
                            btnDeleteClick.onDeleteClick(product)
                            return true
                        }

                        throw IllegalArgumentException("Unknown option: ${item?.itemId}")
                    }

                })

                popupMenu.show()
            }
        }
    }

    class WiderViewHolder(
        private val binding: ProductItemManageWiderBinding,
        private val btnUpdateClick: BtnUpdateClickInterface,
        private val btnDeleteClick: BtnDeleteClickInterface
    ) : ViewHolder(binding.root) {

        private lateinit var myProduct: Product

        fun setData(product: Product) {
            myProduct = product
            binding.productName.text = product.name
            binding.productPrice.text = product.price.toString()
            binding.btnUpdate.setOnClickListener { btnUpdateClick.onUpdateClick(product, it) }
            binding.btnDelete.setOnClickListener { btnDeleteClick.onDeleteClick(product, it) }
        }
    }

    fun setData(productList: List<Product>) {
        this.productList = productList
        this.productListFiltered = this.productList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val density = parent.resources.displayMetrics.density
        screenWidthInDp = parent.resources.displayMetrics.widthPixels / density

        if (screenWidthInDp < 600) {


            val binding = ProductItemManageNarrowerBinding.inflate(inflater, parent, false)
            return NarrowerViewHolder(
                binding,
                btnUpdateClick,
                btnDeleteClick
            )

        }


        val binding = ProductItemManageWiderBinding.inflate(inflater, parent, false)
        return WiderViewHolder(
            binding,
            btnUpdateClick,
            btnDeleteClick
        )

    }

    override fun getItemCount(): Int {
        return productListFiltered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (screenWidthInDp < 600) {
            (holder as NarrowerViewHolder).setData(productListFiltered[position])

        } else {
            (holder as WiderViewHolder).setData(productListFiltered[position])

        }
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val constraintString = constraint?.toString() ?: ""

                productListFiltered = if (constraintString.isEmpty()) {
                    productList
                } else {
                    productList.filter { product ->
                        // product.name.startsWith(constraintString)
                        product.name.contains(constraintString, ignoreCase = true)
                    }
                }

                return FilterResults().apply {
                    values = productListFiltered
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }

        }
    }
}