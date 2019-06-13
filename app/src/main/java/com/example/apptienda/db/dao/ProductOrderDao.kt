package com.example.apptienda.db.dao

import android.arch.persistence.room.*
import android.database.sqlite.SQLiteConstraintException
import com.example.apptienda.db.models.ProductOrder

@Dao
interface ProductOrderDao {
    @Query("SELECT * FROM productorders WHERE order_id LIKE :order_id")
    fun getOrder(order_id: Int): ProductOrder?

    @Query("SELECT * FROM productorders WHERE product_id LIKE :product_id")
    fun getProduct(product_id: Int): ProductOrder?


    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(productOrder: ProductOrder)

}