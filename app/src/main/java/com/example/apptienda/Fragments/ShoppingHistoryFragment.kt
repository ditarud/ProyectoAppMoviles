package com.example.apptienda.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apptienda.Adapters.ShoppingHistoryAdapter
import com.example.apptienda.LoginActivity
import kotlinx.android.synthetic.main.fragment_shopping_history.*
import com.example.apptienda.R
import com.example.apptienda.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ShoppingHistoryFragment : Fragment() {

    var actualUserId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_shopping_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onStart() {
        super.onStart()
        if (LoginActivity.admin == true) {
            loadAllOrders()
        } else {
            loadOrders()
        }
    }

    private fun loadAllOrders() {
        val orderDao = AppDatabase.getDatabase(context!!).orderDao()
        GlobalScope.launch(Dispatchers.IO) { // replaces doAsync (runs on another thread)

            val orders = orderDao.getAllOrders()
            launch(Dispatchers.Main) {// replaces uiThread (runs on UIThread)
                val itemsAdapter = ShoppingHistoryAdapter(context!!, ArrayList(orders))
                orderListView.adapter = itemsAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun loadOrders() {
        val orderDao = AppDatabase.getDatabase(context!!).orderDao()
        val productOrderDao = AppDatabase.getDatabase(context!!).productOrderDao()
        GlobalScope.launch(Dispatchers.IO) { // replaces doAsync (runs on another thread)

            actualUserId = AppDatabase.getDatabase(context!!).userDao().getUser(LoginActivity.actualEmail)!!.id

            val orders = orderDao.getAllUserOrder(actualUserId)
            launch(Dispatchers.Main) {// replaces uiThread (runs on UIThread)
                val itemsAdapter = ShoppingHistoryAdapter(context!!, ArrayList(orders))
                orderListView.adapter = itemsAdapter
            }
        }
    }
}
