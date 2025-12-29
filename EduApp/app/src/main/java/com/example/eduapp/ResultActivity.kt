package com.example.eduapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eduapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("USER_ID", 0)

        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 0)
        binding.tvScore.text = getString(R.string.score_format, score, total)

        binding.btnRetry.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java).apply { putExtra("USER_ID", userId) })
            finish()
        }

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply { putExtra("USER_ID", userId) })
            finish()
        }
    }
}
