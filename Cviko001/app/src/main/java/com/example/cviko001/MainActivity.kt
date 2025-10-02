package com.example.cviko001

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etCity = findViewById<EditText>(R.id.etCity)
        val etAge = findViewById<EditText>(R.id.etAge)
        val tvInformation = findViewById<TextView>(R.id.tvInformation)
        val btnSend = findViewById<Button>(R.id.btSend)
        val btnDelete = findViewById<Button>(R.id.btdelete)

        // nastaveni obsluhy pro tlacitko send

        btnSend.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val city = etCity.text.toString()
            val age = etAge.text.toString().toIntOrNull() ?: 0

            val info = "My name is $firstName $lastName, I am $age years old and I live in $city."
            tvInformation.text = info
        }
        btnDelete.setOnClickListener {
            etFirstName.text.clear()
            etLastName.text.clear()
            etCity.text.clear()
            etAge.text.clear()
            tvInformation.text = ""
        }
    }
}