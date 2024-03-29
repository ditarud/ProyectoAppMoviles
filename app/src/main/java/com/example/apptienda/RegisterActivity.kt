package com.example.apptienda

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
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

    val userList: MutableList<String> = ArrayList()
    var userEmailDB = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        checkUserExist()
        setListeners()
    }


    private fun setListeners() {
        registerButton.setOnClickListener {

            if (TextUtils.isEmpty(userNameEditText.text) || TextUtils.isEmpty(lastNameEditText.text) || TextUtils.isEmpty(
                    addressEditText.text
                )
                || TextUtils.isEmpty(emailEditText.text) || TextUtils.isEmpty(passwordEditText.text) || TextUtils.isEmpty(
                    userNameEditText.text
                )
            ) {
                Toast.makeText(applicationContext, "Fields should not be empty", Toast.LENGTH_SHORT).show()

            } else if (!EmailValidator.isValidEmail(emailEditText.text.toString())) {

                Toast.makeText(this, R.string.emailError, Toast.LENGTH_LONG).show()

            } else if (emailEditText.text.toString() in checkUserExist()) {
                Toast.makeText(this, R.string.emailExist, Toast.LENGTH_LONG).show()

            } else {
                createUser()
                startActivityForResult(
                    Intent(this, MainActivity::class.java),
                    RequestCode.GO_TO_REGISTER_FROM_LOGIN_ACTIVITY.value)

            }
        }
    }


    private fun checkUserExist() : MutableList<String>{
        GlobalScope.launch(Dispatchers.IO) {
            // replaces doAsync (runs on another thread)
            for (i in 0..AppDatabase.getDatabase(applicationContext).userDao().getAllUser().count() - 1) {
                userEmailDB = AppDatabase.getDatabase(applicationContext).userDao().getAllUser()[i].email.toString()
                userList.add(userEmailDB)
            }
        }

        return userList
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
