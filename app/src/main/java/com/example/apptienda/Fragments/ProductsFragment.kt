package com.example.apptienda.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.apptienda.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


import com.example.apptienda.Adapters.ProductAdapter
import com.example.apptienda.db.models.Product
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.android.synthetic.main.fragment_shopping_history.*
import java.lang.Exception
import com.example.apptienda.*
import com.example.apptienda.R


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
        GlobalScope.launch(Dispatchers.IO) {
            if (LoginActivity.admin != true) {
                createProductButton.visibility = View.GONE
            }
        }
        loadProducts()
        setListOnClickListeners()
        createNewProduct()
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun createNewProduct(){
        createProductButton.setOnClickListener {
            val intent =  Intent(context!!, CreateProductActivity::class.java)
            startActivityForResult(intent, RequestCode.GO_TO_CREATE_PRODUCT_FROM_PRODUCT_FRAG.value)
        }
    }

    private fun loadProducts(){
        val totalProducts = AppDatabase.getDatabase(context!!).productDao()

        GlobalScope.launch(Dispatchers.IO) {
            val products = totalProducts.getAll()
            launch(Dispatchers.Main){
                val itemsAdapter = ProductAdapter(context!!, ArrayList(products))
                productsListView.adapter = itemsAdapter
            }
        }
    }

    private fun setListOnClickListeners(){
        productsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedProduct = (productsListView.adapter).getItem(position) as Product
            startActivity(
                Intent(context, ProductDetailActivity::class.java).
                    putExtra("PRODUCT_ID", selectedProduct.id))
        }
    }


}
