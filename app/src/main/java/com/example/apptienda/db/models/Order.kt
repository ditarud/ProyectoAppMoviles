package com.example.apptienda.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.ForeignKey.SET_NULL
import android.support.annotation.NonNull

@Entity(tableName = "orders", foreignKeys = [
        ForeignKey(entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"),
        onDelete = SET_NULL)
])
data class Order(
    @NonNull @ColumnInfo(name = "user_id") val user_id: Int?,
    @NonNull @ColumnInfo(name = "date") val date: String?,
    @NonNull @ColumnInfo(name = "code") val code: String?,
    @NonNull @ColumnInfo(name = "payment") val payment: String?,
    @ColumnInfo(name = "name") val name: String?
) {
    @PrimaryKey(autoGenerate=true) var id: Int = 0
}