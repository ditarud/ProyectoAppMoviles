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

class ProductDetailActivity : AppCompatActivity() {

    private var currentProductId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        getCurrentProductValues()
    }



    override fun onResume() {
        super.onResume()
        setViewContent()

    }

    private fun getCurrentProductValues() {
        currentProductId = intent.getIntExtra("PRODUCT_ID", 0)
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
                priceTextView.text = selectedProduct!!.price.toString()




              //  val photoUri = selectedProduct.photoUri
                // A post sent to the view in order for it to wait until the view has been rendered (draw)
             //   imagePlaceholder.post {
             //       if (!selectedProduct.photoUri.isNullOrBlank())
             //           setPic(photoUri!!)
             //   }

            }
        }
    }
}

