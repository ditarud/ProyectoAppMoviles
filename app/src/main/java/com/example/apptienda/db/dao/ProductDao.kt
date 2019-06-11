package com.example.apptienda.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.apptienda.db.models.User
import com.example.apptienda.db.models.Order
import com.example.apptienda.db.models.Product
import com.example.apptienda.db.models.ProductImage
import com.example.apptienda.db.models.ProductOrder

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): List<Product>
//
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg product: Product)

//    @Delete
//    fun delete(user: User)
}