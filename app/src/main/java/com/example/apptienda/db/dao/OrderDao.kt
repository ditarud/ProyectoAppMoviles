package com.example.apptienda.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import com.example.apptienda.db.models.Order
import com.example.apptienda.db.models.ProductOrder

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE id LIKE :id")
    fun getOrder(id: Int): Order?

    @Query("SELECT o.id, o.user_id, h.name, o.date, o.code, o.payment  FROM orders o, productorders p, products h WHERE o.user_id LIKE :user AND p.order_id LIKE o.id AND p.product_id LIKE h.id")
    fun getAllUserOrder(user: Int): List<Order>

    @Query("SELECT o.id, o.user_id, h.name, o.date, o.code, o.payment FROM orders o, productorders p, products h WHERE p.order_id LIKE o.id AND p.product_id LIKE h.id")
    fun getAllOrders(): List<Order>

    @Query("SELECT * FROM orders WHERE user_id LIKE :user")
    fun getUserOrder(user: Int): Order?

    @Query("SELECT * FROM orders ORDER BY id DESC LIMIT 1")
    fun getLast(): Order?

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(order: Order)

}