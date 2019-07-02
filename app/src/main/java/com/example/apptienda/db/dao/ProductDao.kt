package com.example.apptienda.db.dao

import android.arch.persistence.room.*
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

    @Query("UPDATE products SET name= :name, price= :price, description= :description, stock= :stock, photo= :photo WHERE id = :id")
    fun update(id: Int, name: String, price :Int, description :String, stock :Int, photo :String)

    @Query("DELETE FROM products WHERE id = :id")
    fun delete(id: Int)
}