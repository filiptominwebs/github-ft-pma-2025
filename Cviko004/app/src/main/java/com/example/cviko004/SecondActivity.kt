package com.example.cviko004

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)
        val twInfo = findViewById<android.widget.TextView>(R.id.twInfo)
        val nickname = intent.getStringExtra("NICK_NAME")
        twInfo.text = "Your nickname is: $nickname"

        val btnBack = findViewById<android.widget.Button>(R.id.btnCloseActivity)
        btnBack.setOnClickListener {
            finish()
        }
    }
}