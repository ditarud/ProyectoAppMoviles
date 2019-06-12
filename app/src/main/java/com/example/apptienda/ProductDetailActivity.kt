package com.example.apptienda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.apptienda.db.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.widget.Toast
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_product_detail.radioGroup
import kotlinx.android.synthetic.main.activity_register.*


class ProductDetailActivity : AppCompatActivity() {

    private var currentProductId: Int = 0
    private var productPhoto: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        getCurrentProductValues()
    }



    override fun onResume() {
        super.onResume()
        setViewContent()
        buyButton.setOnClickListener {
            confirmDialogDemo()
        }

    }

    private fun getCurrentProductValues() {
        currentProductId = intent.getIntExtra("PRODUCT_ID", 0)
        //productPhoto = AppDatabase.getDatabase(this).productImageDao().getProductImage(currentProductId)!!.path.toString()
    }

    private fun setViewContent() {
        GlobalScope.launch(Dispatchers.IO) {
            val currentUserEmail = LoginActivity.actualEmail
            val appDatabase = AppDatabase.getDatabase(baseContext)
            val productDao = appDatabase.productDao()
            val selectedProduct = productDao.getProduct(currentProductId)




            launch(Dispatchers.Main) {
                nameTextView.text = selectedProduct!!.name
                descriptionTextView.text = selectedProduct!!.description
                priceTextView.text = "$" + selectedProduct!!.price.toString()





              //  val photoUri = selectedProduct.photoUri
                // A post sent to the view in order for it to wait until the view has been rendered (draw)
             //   imagePlaceholder.post {
             //       if (!selectedProduct.photoUri.isNullOrBlank())
             //           setPic(photoUri!!)
             //   }

            }
        }
    }

    private fun alertDialogDemo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert dialog !")
        builder.setMessage("No payment method selected!")
        builder.setCancelable(true)
        builder.setNeutralButton(
            "Ok"
        ) { dialog, which ->

        }
        builder.show()
    }

    private fun confirmDialogDemo() {


        if (radioGroup.checkedRadioButtonId != -1) {
            val radio: RadioButton = findViewById(radioGroup.checkedRadioButtonId)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm dialog demo !")
            builder.setMessage("Are you sure buying this product ?" + "\n" + "Price : " + priceTextView.text.toString() +
                    "\n" + "Method : " + "${radio.text}")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which ->

                })

            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, which ->

                })

            builder.show()
        } else {
            alertDialogDemo()
        }


    }

}

