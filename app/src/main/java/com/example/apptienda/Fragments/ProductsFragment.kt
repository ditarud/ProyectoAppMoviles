package com.example.apptienda.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.apptienda.R
import com.example.apptienda.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


import com.example.apptienda.Adapters.ProductAdapter
import com.example.apptienda.db.models.Product
import kotlinx.android.synthetic.main.fragment_products.*
import java.lang.Exception


class ProductsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadProducts()
        //setListOnClickListeners()
    }

    private fun loadProducts(){
        val totalProducts = AppDatabase.getDatabase(context!!).productDao()

        GlobalScope.launch(Dispatchers.IO) {  // replaces doAsync (runs on another thread)
            val cant = totalProducts.getAll()
            if (cant.size == 0){
                try {
                    val firstp = createProductObject()
                    totalProducts.insert(firstp)
                    launch(Dispatchers.Main) {
                        Toast.makeText(context!!, "Saved successfully the product", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    launch(Dispatchers.Main) {
                        Log.d("ERROR", "Error storing product ${e.message}")
                        Toast.makeText(context!!, "Error storing example products ${e.message}" , Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //val productsDao = AppDatabase.getDatabase(context!!).productDao()
        GlobalScope.launch(Dispatchers.IO) {
            val products = totalProducts.getAll()
            launch(Dispatchers.Main){
                val itemsAdapter = ProductAdapter(context!!, ArrayList(products))
                productsListView.adapter = itemsAdapter
            }
        }
    }

    private fun setListOnClickListeners(){

    }

    private fun createProductObject(): Product{
        val name = "Example"
        val price = 1000
        val description = "the very first product"
        val stock = 1000
        val deleted = false
        return Product(name, price, description, stock, deleted)
    }



}
