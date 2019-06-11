package com.example.apptienda.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "users")
data class User(
    @NonNull @ColumnInfo(name = "name") val name: String?,
    @NonNull @ColumnInfo(name = "lastname") val lastname: String?,
    @NonNull @ColumnInfo(name = "address") val address: String?,
    @NonNull @ColumnInfo(name = "role") val role: Boolean?,
    @NonNull @ColumnInfo(name = "email") val email: String?,
    @NonNull @ColumnInfo(name = "password") val password: String?,
    @NonNull @ColumnInfo(name = "deleted") val deleted: Boolean?
) {
    @PrimaryKey(autoGenerate=true) var id: Int = 0
}