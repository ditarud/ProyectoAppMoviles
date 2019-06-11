package com.example.apptienda.db.dao

import android.arch.persistence.room.*
import android.database.sqlite.SQLiteConstraintException
import com.example.apptienda.db.models.ProductImage

@Dao
interface ProductImageDao {
    @Query("SELECT * FROM productimages WHERE product_id LIKE :product_id")
    fun getProductImage(product_id: Int): ProductImage?

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(productImage: ProductImage)

    @Update(onConflict = OnConflictStrategy.FAIL)
    fun update(productImage: ProductImage)

    @Delete
    fun deleteProductImage(productImage: ProductImage)

}