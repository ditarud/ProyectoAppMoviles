package com.example.apptienda

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.apptienda.db.AppDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    var userEmailDB = ""
    val userList: MutableList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkUserExist()
        setListeners()
        goToRegisterActivity()
    }

    companion object {
        var actualEmail = ""
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

    private fun setListeners() {
        loginButton.setOnClickListener {
            val userEmail = emailEdit.text.toString()
            val userPassword = passwordEditText.text.toString()
            when {
                // Check Email
                !EmailValidator.isValidEmail(userEmail)  ->
                    Toast.makeText(this, R.string.emailError, Toast.LENGTH_LONG).show()
                userEmail  !in checkUserExist() ->
                    Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_LONG).show()
                // Check Password
                userPassword.isNullOrEmpty() ->
                    Toast.makeText(this, R.string.passwordError, Toast.LENGTH_LONG).show()
                // Go to MainActivity
                else -> {
                    actualEmail = userEmail
                    // Add data to invoking intent
                    intent.apply {
                        putExtra("EMAIL", userEmail)
                        putExtra("PASSWORD", userPassword)
                    }
                    // Set response
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun goToRegisterActivity(){
        goToRegisterFormButton.setOnClickListener {
            startActivityForResult(
                Intent(this, RegisterActivity::class.java),
                RequestCode.GO_TO_REGISTER_FROM_LOGIN_ACTIVITY.value)
        }
    }

    override fun onBackPressed() {
        // Do nothing as we don't want it to go back to MainActivity after SingOut
    }
}
