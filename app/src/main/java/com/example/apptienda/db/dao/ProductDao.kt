package com.example.apptienda.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
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

    @Query("SELECT * FROM products WHERE id LIKE :id")
    fun getProduct(id: Int): Product?

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(product: Product)

    @Insert
    fun insertAll(vararg product: Product)

//    @Delete
//    fun delete(user: User)
}