package com.example.apptienda

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        saveData()
    }

    private fun saveData() {
        registerButton.setOnClickListener {
            // Guardar datos en BD
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        }
    }
}
