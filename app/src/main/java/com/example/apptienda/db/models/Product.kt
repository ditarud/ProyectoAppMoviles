package com.example.apptienda.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "products")
data class Product(
    @NonNull @ColumnInfo(name = "name") val name: String?,
    @NonNull @ColumnInfo(name = "price") val price: Int?,
    @NonNull @ColumnInfo(name = "description") val description: String?,
    @NonNull @ColumnInfo(name = "stock") val stock: Int?,
    @NonNull @ColumnInfo(name = "photo") val photo: String?,
    @NonNull @ColumnInfo(name = "deleted") val deleted: Boolean?
) {
    @PrimaryKey(autoGenerate=true) var id: Int = 0
}