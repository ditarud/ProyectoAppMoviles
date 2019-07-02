package com.example.apptienda

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.apptienda.Adapters.ProductAdapter
import com.example.apptienda.db.AppDatabase
import com.example.apptienda.db.models.Product
import com.example.apptienda.db.models.ProductImage
import kotlinx.android.synthetic.main.activity_create_product.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CreateProductActivity : AppCompatActivity() {

    private var currentPhotoPath: String? = null // temp store of photo path
    private var state: String = "create"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)
        setListener()
    }

    private fun setListener() {
        val extras = intent.extras
        if (extras != null) {
            loadProduct(extras!!.getInt("PRODUCT_ID"))
            state = "edit"
            confirmProductCreationButton.text = "Edit product"
        }
        confirmProductCreationButton.setOnClickListener {
            if (state == "create") {
                createProduct()
            }  else {
                updateProductInBD(extras!!.getInt("PRODUCT_ID"))
                finish()
                }
            }

        takePictureImageButton.setOnClickListener {
            if (hasCamera()) {
                dispatchToCameraActivity()
            } else {
                Toast.makeText(this, "Your device doesn't have a camera built in", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateProductInBD(productId: Int){
        val totalProducts = AppDatabase.getDatabase(this).productDao()
        val name = nameEditText.text.toString()
        val price = Integer.parseInt(priceEditText.text.toString())
        val description = descriptionEditText.text.toString()
        val stock = Integer.parseInt(stockEditText.text.toString())


        GlobalScope.launch(Dispatchers.IO) {  // replaces doAsync (runs on another thread)
            try {


                totalProducts.update(productId, name, price, description, stock, currentPhotoPath!!)

                launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Saved successfully the product", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.d("ERROR", "Error storing product ${e.message}")
                    Toast.makeText(applicationContext, "Error storing example products ${e.message}" , Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun saveProductToBD(product: Product) {
        val totalProducts = AppDatabase.getDatabase(this).productDao()

        GlobalScope.launch(Dispatchers.IO) {  // replaces doAsync (runs on another thread)
                try {

                    totalProducts.insert(product)
                    launch(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Saved successfully the product", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    launch(Dispatchers.Main) {
                        Log.d("ERROR", "Error storing product ${e.message}")
                        Toast.makeText(applicationContext, "Error storing example products ${e.message}" , Toast.LENGTH_LONG).show()
                    }
                }

        }
    }


    private fun createProduct() {
        val productObject = createProductObject()
        saveProductToBD(productObject)

    }

    /* PARA EL EDITAR , AGREGAR MAS FOTOS ALGO ASÍ
    private fun createProductImageObject(productId: Int): ProductImage {
        val productId = productId
        val photoPath = currentPhotoPath
        return ProductImage(productId,photoPath)
    } */

    private fun createProductObject(): Product {
        val name = nameEditText.text.toString()
        val price = Integer.parseInt(priceEditText.text.toString())
        val description = descriptionEditText.text.toString()
        val stock = Integer.parseInt(stockEditText.text.toString())
        val deleted = false
        return Product(name, price, description, stock,currentPhotoPath, deleted)
    }



   /* private fun createProductObject2(): Product{
        val name = "Example"
        val price = 1000
        val description = "the very first product"
        val stock = 3
        val deleted = false
        return Product(name, price, description, stock, deleted)
    }
 */

    private fun loadProduct(productid: Int) {
        val totalProducts = AppDatabase.getDatabase(this).productDao()

        GlobalScope.launch(Dispatchers.IO) {
            val product = totalProducts.getProduct(productid)
            launch(Dispatchers.Main){
                nameEditText.setText(product!!.name)
                priceEditText.setText(Integer.toString(product.price!!))
                descriptionEditText.setText(product.description)
                stockEditText.setText(Integer.toString(product.stock!!))
                setPic(product.photo.toString())

            }
        }
    }






    private fun hasCamera(): Boolean {
        return this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    /**
     *  Starting activity with the correct fileURI in which the photo will be stored
     */
    private fun dispatchToCameraActivity() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(this.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("ERROR", "Error creating file: ${ex.message}")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, RequestCode.REQUEST_TAKE_PHOTO.value)
                }
            }
        }
    }

    /**
     *  Creating temporary File in which the image will be stored
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    /**
     *  Handle response from ACTION_IMAGE_CAPTURE Intent AND Edit product
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_TAKE_PHOTO.value && resultCode == Activity.RESULT_OK) {
            setPic()
        }



    }

    /**
     *  Load Picture into ImageButton
     */
    private fun setPic() {
        // Get the dimensions of the View
        takePictureImageButton.setPadding(0, 0, 0, 0)
        val targetW: Int = takePictureImageButton.width
        val targetH: Int = takePictureImageButton.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(currentPhotoPath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            val correctedBitmap = rotate(bitmap,90F)
            takePictureImageButton.setImageBitmap(correctedBitmap)
        }
    }

    private fun setPic(imagePath: String) {
        // Get the dimensions of the View
        takePictureImageButton.setPadding(0, 0, 0, 0)
        val targetW: Int = takePictureImageButton.width
        val targetH: Int = takePictureImageButton.height

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
            takePictureImageButton.setImageBitmap(correctedBitmap)
        }
    }

    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val w = bitmap.width
        val h = bitmap.height

        val mtx = Matrix()
        mtx.postRotate(degree)

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
    }



}
