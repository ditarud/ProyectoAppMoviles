package com.example.apptienda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.apptienda.db.AppDatabase
import com.example.apptienda.db.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setListeners()
    }

    private fun setListeners() {
        registerButton.setOnClickListener {
            createUser()
            finish()

        }
    }


    private fun createUser() {
        val userObject = createUserObject()
        saveData(userObject)
    }

    private fun saveData(user: User){

        val complaintDao = AppDatabase.getDatabase(this).userDao()
        GlobalScope.launch(Dispatchers.IO) {  // replaces doAsync (runs on another thread)
            try {
                complaintDao.insert(user)
                launch(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Saved successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Log.d("ERROR", "Error storing complaint ${e.message}")
                    Toast.makeText(applicationContext, "Error storing complaint ${e.message}" , Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createUserObject(): User {
        val name = userNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val address = addressEditText.text.toString()
        val password = passwordEditText.text.toString()
        val email = emailEditText.text.toString()
        val role = false
        val deleted = false
        return User(name, lastName, address, role, email, password, deleted)
    }
}
