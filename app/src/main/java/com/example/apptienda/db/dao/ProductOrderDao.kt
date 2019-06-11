package com.example.apptienda.db.dao

import android.arch.persistence.room.*
import android.database.sqlite.SQLiteConstraintException
import com.example.apptienda.db.models.ProductOrder

@Dao
interface ProductOrderDao {
    @Query("SELECT * FROM productorders WHERE id LIKE :id")
    fun getOrder(id: Int): ProductOrder?

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(productOrder: ProductOrder)

}