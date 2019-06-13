package com.example.apptienda

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.apptienda.Fragments.ProductsFragment
import com.example.apptienda.Fragments.ShoppingHistoryFragment
import com.example.apptienda.db.AppDatabase
import kotlinx.android.synthetic.main.activity_summary_order.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class SummaryOrderActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_order)
        setView()
        loadActualOrder()
    }

    override fun onResume() {
        super.onResume()
        loadActualOrder()
    }


    //CAMBIAR EL BACK PRESSED , PARA QUE NO VUELVA AL PRODUCTO , SINO QUE RETORNE A LISTA DE PRODUCTOS o HOME

    private fun setView(){
        instructionsTextView.text = "Para transferir debes depositar a la cuenta 2123123 , Banco de chile , el monto es .... "

    }

    private fun loadActualOrder() {
        val orderDao = AppDatabase.getDatabase(this).orderDao()
        GlobalScope.launch(Dispatchers.IO) { // replaces doAsync (runs on another thread)
            val actualOrder = orderDao.getAllUserOrder(ProductDetailActivity.actualUserIdGlobal)
            launch(Dispatchers.Main) {// replaces uiThread (runs on UIThread)
                orderNumberTextView.text = actualOrder!![actualOrder.lastIndex].code
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    // Obtener Precio para mostar en resumen
    /* private fun getProductOrder(){
        val productOrderDao = AppDatabase.getDatabase(this).productOrderDao()
        GlobalScope.launch(Dispatchers.IO) { // replaces doAsync (runs on another thread)
            val actualProduct = productOrderDao.getProduct()
            launch(Dispatchers.Main) {// replaces uiThread (runs on UIThread)
                orderNumberTextView.text = actualProduct!!.code

            }
        }
    }*/




}
