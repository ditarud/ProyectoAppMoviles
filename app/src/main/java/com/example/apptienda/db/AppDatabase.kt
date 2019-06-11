package com.example.apptienda.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.apptienda.db.dao.ProductDao
import com.example.apptienda.db.dao.UserDao
import com.example.apptienda.db.dao.OrderDao
import com.example.apptienda.db.dao.ProductImageDao
import com.example.apptienda.db.dao.ProductOrderDao
import com.example.apptienda.db.models.User
import com.example.apptienda.db.models.Order
import com.example.apptienda.db.models.Product
import com.example.apptienda.db.models.ProductImage
import com.example.apptienda.db.models.ProductOrder

@Database(entities = [Product::class, User::class, Order::class, ProductImage::class, ProductOrder::class], version = 1, exportSchema = false)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao
    abstract fun productOrderDao(): ProductOrderDao
    abstract fun productImageDao(): ProductImageDao


    // Add Singleton to prevent having multiple instances
    // of the database opened at the same time.
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}