package com.example.dailycaloriestracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dailycaloriestracker.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var addMealLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Register launcher to receive result from AddMealActivity
        addMealLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // If a meal was added, open the MealsOverviewActivity so the user sees it
                val userId = intent?.getLongExtra("USER_ID", 0L) ?: 0L
                val overviewIntent = Intent(this@HomeActivity, MealsOverviewActivity::class.java).apply {
                    putExtra("USER_ID", userId)
                }
                startActivity(overviewIntent)
            }
        }

        binding.btnSignOut.setOnClickListener {
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.btnAddMeal.setOnClickListener {
            val userId = intent?.getLongExtra("USER_ID", 0L) ?: 0L
            val intent = Intent(this@HomeActivity, AddMealActivity::class.java).apply {
                putExtra("USER_ID", userId)
            }
            addMealLauncher.launch(intent)
        }

        binding.btnMealsOverview.setOnClickListener {
            val userId = intent?.getLongExtra("USER_ID", 0L) ?: 0L
            val intent = Intent(this@HomeActivity, MealsOverviewActivity::class.java).apply {
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }
    }
}