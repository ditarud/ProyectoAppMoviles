package com.example.apptienda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.apptienda.db.models.Order
import com.example.apptienda.R


class ShoppingHistoryAdapter (
    context: Context,
    private val dataSource: ArrayList<Order>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item_shopping_history, parent, false)
        rowView.findViewById<TextView>(R.id.orderDateTextView).text = dataSource[position].date
        rowView.findViewById<TextView>(R.id.orderPaymentTextView).text = dataSource[position].payment
        rowView.findViewById<TextView>(R.id.orderCodeTextView).text = "Order: " + dataSource[position].code
        rowView.findViewById<TextView>(R.id.productNameTextView).text = "Product Name: " + dataSource[position].name




        return rowView
    }
}

