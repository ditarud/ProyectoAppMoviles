package com.example.apptienda.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.support.annotation.NonNull

@Entity(tableName = "productorders", foreignKeys = [
        ForeignKey(entity = Product::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("product_id"),
        onDelete = CASCADE),

        ForeignKey(entity = Order::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("order_id"),
        onDelete = CASCADE)
])
data class ProductOrder(
    @NonNull @ColumnInfo(name = "product_id") val product_id: Int?,
    @NonNull @ColumnInfo(name = "order_id") val order_id: Int?,
    @NonNull @ColumnInfo(name = "amount") val amount: Int?
) {
    @PrimaryKey(autoGenerate=true) var id: Int = 0
}