package com.example.apptienda.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.apptienda.R
import com.example.apptienda.db.models.Product

class ProductAdapter(
    context: Context,
    private val dataSource: ArrayList<Product>) : BaseAdapter() {

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
        val rowView = inflater.inflate(R.layout.list_item_product, parent, false)
        rowView.findViewById<TextView>(R.id.nameTextView).text = dataSource[position].name
        rowView.findViewById<TextView>(R.id.priceTextView).text = (dataSource[position].price).toString()
        rowView.findViewById<TextView>(R.id.descriptionTextView).text = dataSource[position].description
        rowView.findViewById<TextView>(R.id.stockTextView).text = dataSource[position].stock.toString()

        return rowView
    }
}