package com.manuelsoft.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.manuelsoft.app.databinding.MainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    private lateinit var binding: MainBinding
    private lateinit var listAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupProductsList()
        setupSearchBar()
    }

    private fun setupProductsList() {
        val products = listOf( "Elemento 1", "Elemento 2", "Elemento 3")
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, products)
        binding.productsList.adapter = listAdapter
    }


    private fun setupSearchBar() {
        binding.etSearchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listAdapter.filter.filter(s);
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })
    }

}