package com.example.eduapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eduapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var userId: Long = 0
    private lateinit var adapter: ResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("USER_ID", 0)
        adapter = ResultAdapter()
        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = adapter

        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java).apply { putExtra("USER_ID", userId) })
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadResults()
    }

    private fun loadResults() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(applicationContext)
            val results = withContext(Dispatchers.IO) { db.resultDao().getResultsForUser(userId) }
            adapter.submitList(results)
        }
    }
}

