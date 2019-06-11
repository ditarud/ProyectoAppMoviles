package com.example.apptienda.db.dao

import android.arch.persistence.room.*
import android.database.sqlite.SQLiteConstraintException
import com.example.apptienda.db.models.User


@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email LIKE :email")
    fun getUser(email: String): User?

    @Query("SELECT * FROM users")
    fun getAllUser() : List<User>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(user: User)

    @Update(onConflict = OnConflictStrategy.FAIL)
    fun update(user: User)

    @Delete
    fun deleteUser(user: User)

}