package com.example.apptienda

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_shopping_history.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.net.URL


class ShoppingHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_shopping_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productNameTextView.text = "Computador 1"
        orderDateTextView.text = "2019-10-12"
        amountPaidTextView.text = "$23.000"
        productPhotoImageView.setImageResource(R.mipmap.ic_launcher)



    }

    override fun onStart() {
        super.onStart()
        setCredentials()
    }

    private fun setCredentials() {

    }
}
