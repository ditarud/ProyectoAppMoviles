package com.example.apptienda.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.apptienda.R
import com.example.apptienda.db.models.Product
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    private fun setPic(imagePath: String) {
        // Get the dimensions of the View

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item_product, parent, false)
        rowView.findViewById<TextView>(R.id.nameTextView).text = dataSource[position].name
        rowView.findViewById<TextView>(R.id.priceTextView).text = (dataSource[position].price).toString()
        rowView.findViewById<TextView>(R.id.descriptionTextView).text = dataSource[position].description
        rowView.findViewById<TextView>(R.id.stockTextView).text = dataSource[position].stock.toString()
        val  imgFinal =  dataSource[position].photo

        GlobalScope.launch(Dispatchers.IO) {

            launch(Dispatchers.Main) {
                if (imgFinal != null) {
                    val targetW: Int = rowView.findViewById<ImageView>(R.id.productPhotoImageView).width
                    val targetH: Int = rowView.findViewById<ImageView>(R.id.productPhotoImageView).height

                    val bmOptions = BitmapFactory.Options().apply {
                        // Get the dimensions of the bitmap
                        inJustDecodeBounds = true
                        BitmapFactory.decodeFile(imgFinal, this)
                        val photoW: Int = outWidth
                        val photoH: Int = outHeight

                        // Determine how much to scale down the image
                        if (targetH != 0 && targetW != 0) {
                            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)
                            inJustDecodeBounds = false
                            inSampleSize = scaleFactor
                        }
                        // Decode the image file into a Bitmap sized to fill the View

                    }



                    BitmapFactory.decodeFile(imgFinal, bmOptions)?.also { bitmap ->
                        val correctedBitmap = rotate(bitmap,90F)
                        rowView.findViewById<ImageView>(R.id.productPhotoImageView).setImageBitmap(correctedBitmap)
                        rowView.findViewById<ImageView>(R.id.productPhotoImageView).alpha = 1f
                    }


                } else {
                    rowView.findViewById<ImageView>(R.id.productPhotoImageView).setImageResource(R.drawable.no_image)
                }
            }
        }

        return rowView
    }
}



private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
    val w = bitmap.width
    val h = bitmap.height

    val mtx = Matrix()
    mtx.postRotate(degree)

    return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
}