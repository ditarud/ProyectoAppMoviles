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
import android.util.Log
import android.widget.RadioButton
import com.example.apptienda.db.models.Order
import com.example.apptienda.db.models.ProductOrder
import kotlinx.android.synthetic.main.activity_product_detail.radioGroup
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.list_item_shopping_history.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class ProductDetailActivity : AppCompatActivity() {

    private var currentProductId: Int = 0
    private var productPhoto: String = ""
    var actualUserId = 0
    var random = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        getCurrentProductValues()

    }
    companion object {
        var actualUserIdGlobal = 0
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
                    createOrder()

                    val intent = Intent(applicationContext, SummaryOrderActivity::class.java)
                    startActivity(intent)
                    finish()
                })

            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, which ->

                })

            builder.show()
        } else {
            alertDialogDemo()
        }


    }



    private fun createOrder() {
        GlobalScope.launch(Dispatchers.IO) {
            actualUserId = AppDatabase.getDatabase(applicationContext).userDao().getUser(LoginActivity.actualEmail)!!.id
            actualUserIdGlobal = actualUserId
            val orderObject = createOrderObject(actualUserId)
            val productOrderObject = createProductOrderObject(actualUserId,currentProductId)
            saveOrderData(orderObject)
            saveProductOrderData(productOrderObject)
        }
    }



    private fun createOrderObject(actualUserId : Int): Order {
        val radio: RadioButton = findViewById(radioGroup.checkedRadioButtonId)

        val dateFormatter = SimpleDateFormat("dd-MM-yyyy hh:mm")
        dateFormatter.setLenient(false)
        val today = Date()

        val date = dateFormatter.format(today)
        random = (1000..3000).random()
        val number = random.toString()
        val payment = radio.text.toString()

        return Order(actualUserId, date, number, payment)
    }

    private fun createProductOrderObject(actualUserId: Int, productId: Int): ProductOrder{
        val amount = 1
        return  ProductOrder(productId, actualUserId, amount)

    }

    // ESTO SOLO SE HACE ACÁ YA QUE NO SE PUEDE GENERAR UNA ORDEN ( COMPRAR AUN NO SE IMPLEMENTA)
    private fun saveOrderData(order: Order){

        val orderDao = AppDatabase.getDatabase(this).orderDao()
        GlobalScope.launch(Dispatchers.IO) {  // replaces doAsync (runs on another thread)
            try {
                orderDao.insert(order)
                launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Saved Order successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.d("ERROR", "Error storing order ${e.message}")
                    Toast.makeText(applicationContext!!, "Error storing order ${e.message}" , Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveProductOrderData(productOrderObject: ProductOrder) {
        val productOrderDao = AppDatabase.getDatabase(this).productOrderDao()
        GlobalScope.launch(Dispatchers.IO) {  // replaces doAsync (runs on another thread)
            try {
                productOrderDao.insert(productOrderObject)
                launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Saved Product Order successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.d("ERROR", "Error storing order ${e.message}")
                    Toast.makeText(applicationContext!!, "Error storing order ${e.message}" , Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}

