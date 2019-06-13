package com.example.apptienda.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.apptienda.Adapter.ShoppingHistoryAdapter
import com.example.apptienda.LoginActivity
import kotlinx.android.synthetic.main.fragment_shopping_history.*
import com.example.apptienda.R
import com.example.apptienda.db.AppDatabase
import com.example.apptienda.db.models.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


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
        loadOrders()
    }




    private fun loadOrders() {
        val orderDao = AppDatabase.getDatabase(context!!).orderDao()
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
