package com.manuelsoft.app

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.manuelsoft.app.databinding.ProductItemSearchBinding
import com.manuelsoft.repository.Product

class SearchProductsRecyclerViewAdapter :
    RecyclerView.Adapter<SearchProductsRecyclerViewAdapter.MyViewHolder>(), Filterable {

    private var productList: List<Product> = listOf()
    private var productListFiltered: List<Product> = listOf()

    class MyViewHolder(private val binding: ProductItemSearchBinding) : ViewHolder(binding.root) {

        private lateinit var myProduct: Product

        fun setData(product: Product) {
            myProduct = product
            binding.productName.text = product.name
            binding.productPrice.text = product.price.toString()
        }
    }

    fun setData(productList: List<Product>) {
        this.productList = productList
        this.productListFiltered = this.productList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemSearchBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productListFiltered.size
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.setData(productListFiltered[position])
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