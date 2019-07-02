package com.example.apptienda.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.support.annotation.NonNull

@Entity(tableName = "productimages", foreignKeys = [
        ForeignKey(entity = Product::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("product_id"),
        onDelete = CASCADE)
])
data class ProductImage(
    @NonNull @ColumnInfo(name = "product_id") val product_id: Int?,
    @NonNull @ColumnInfo(name = "path") val path: String?
) {
    @PrimaryKey(autoGenerate=true) var id: Int = 0
}