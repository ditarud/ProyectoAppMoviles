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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.RadioButton
import com.example.apptienda.db.models.Order
import com.example.apptienda.db.models.ProductImage
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
    private var imgFinal: ProductImage? = null
    var actualUserId = 0
    var orId = 0
    var random = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        getCurrentProductValues()
        editProductListener()
        deleteProductListener()

    }
    companion object {
        var actualUserIdGlobal = 0
    }


    override fun onResume() {
        super.onResume()
        setViewContent()
        GlobalScope.launch(Dispatchers.IO) {
            if (LoginActivity.admin == true) {
                buyButton.visibility = View.GONE
            }
            else {
                editProductButton.visibility = View.GONE
                deleteProductButton.visibility = View.GONE
            }
        }
        buyButton.setOnClickListener {
            confirmDialogDemo()
        }
    }

    private fun deleteProductListener() {
        deleteProductButton.setOnClickListener {
            deleteDialogDemo()
        }
    }


    private fun editProductListener(){
        editProductButton.setOnClickListener {
            val intent =  Intent(this, CreateProductActivity::class.java)
            intent.putExtra("PRODUCT_ID", currentProductId)
            startActivity(intent)
        }
    }

    private fun deleteProduct(productId: Int) {
        val totalProducts = AppDatabase.getDatabase(this).productDao()
        GlobalScope.launch(Dispatchers.IO) {  // replaces doAsync (runs on another thread)
            try {

                totalProducts.delete(productId)
                launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Deleted successfully the product", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.d("ERROR", "Error storing product ${e.message}")
                    Toast.makeText(applicationContext, "Error deleted  products ${e.message}" , Toast.LENGTH_LONG).show()
                }
            }

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
            val img = appDatabase.productImageDao()


            launch(Dispatchers.Main) {
                nameTextView.text = selectedProduct!!.name
                descriptionTextView.text = selectedProduct!!.description
                priceTextView.text = "$" + selectedProduct!!.price.toString()
                stockTextView.text = selectedProduct!!.stock.toString()
                val imgFinal = selectedProduct.photo
                //set

                GlobalScope.launch(Dispatchers.IO) {
                    //imgFinal = img.getProductImage(selectedProduct.id)

                    launch(Dispatchers.Main) {
                        if (imgFinal != null) {
                            val path = imgFinal!!
                            setPic(imgFinal)
                        } else {
                            imagePlaceholder.setImageResource(R.drawable.no_image)
                        }
                    }
                }
            }
        }
    }

    private fun setPic(imagePath: String) {
        // Get the dimensions of the View
        imagePlaceholder.setPadding(0, 0, 0, 0)
        val targetW: Int = imagePlaceholder.width
        val targetH: Int = imagePlaceholder.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(imagePath, bmOptions)?.also { bitmap ->
            val correctedBitmap = rotate(bitmap,90F)
            imagePlaceholder.setImageBitmap(correctedBitmap)
        }
    }

    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val w = bitmap.width
        val h = bitmap.height

        val mtx = Matrix()
        mtx.postRotate(degree)

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
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


    private fun deleteDialogDemo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete confirmation !")
        builder.setMessage("Are you sure delete this product ?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, which ->
                deleteProduct(currentProductId)
            })
        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog, which ->

            })
        builder.show()
    }

    private fun createOrder() {
        GlobalScope.launch(Dispatchers.IO) {
            actualUserId = AppDatabase.getDatabase(applicationContext).userDao().getUser(LoginActivity.actualEmail)!!.id
            actualUserIdGlobal = actualUserId
            val orderObject = createOrderObject(actualUserId)
            saveOrderData(orderObject)
            orId = AppDatabase.getDatabase(applicationContext).orderDao().getLast()!!.id
            val productOrderObject = createProductOrderObject(orId, currentProductId)
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

        return Order(actualUserId, date, number, payment, null)
    }

    private fun createProductOrderObject(orderId: Int, productId: Int): ProductOrder{
        val amount = 1
        return  ProductOrder(productId, orderId, amount)

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

