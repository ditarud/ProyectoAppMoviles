package com.example.apptienda.db.dao

import android.arch.persistence.room.*
import android.database.sqlite.SQLiteConstraintException
import com.example.apptienda.db.models.Order

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE id LIKE :id")
    fun getOrder(id: Int): Order?

    @Query("SELECT * FROM orders WHERE user_id LIKE :user")
    fun getUserOrder(user: Int): List<Order>


    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(order: Order)

}