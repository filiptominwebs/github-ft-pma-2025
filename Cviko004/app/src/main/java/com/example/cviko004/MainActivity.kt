package com.example.cviko004

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val btnSecondActivity = findViewById<android.widget.Button>(R.id.btSendData)
        val etNickname = findViewById<android.widget.EditText>(R.id.etNickname)

        btnSecondActivity.setOnClickListener {
            val nickname = etNickname.text.toString()
            val intent = android.content.Intent(this, SecondActivity::class.java)
            intent.putExtra("NICK_NAME", nickname)
            startActivity(intent)
        }
    }
}