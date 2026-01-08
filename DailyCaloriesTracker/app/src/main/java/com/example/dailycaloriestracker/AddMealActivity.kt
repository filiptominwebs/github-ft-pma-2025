package com.example.dailycaloriestracker

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dailycaloriestracker.databinding.ActivityAddMealBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val types = MealType.entries.map { it.name }
        binding.spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim().orEmpty()
            val caloriesText = binding.etCalories.text?.toString().orEmpty()
            val carbsText = binding.etCarbs.text?.toString().orEmpty()
            val proteinText = binding.etProtein.text?.toString().orEmpty()
            val fatText = binding.etFat.text?.toString().orEmpty()
            val typeName = binding.spinnerType.selectedItem as String
            val type = MealType.values().find { it.name == typeName } ?: MealType.OTHER

            if (name.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_enter_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calories = caloriesText.toIntOrNull() ?: 0
            val carbs = carbsText.toIntOrNull() ?: 0
            val protein = proteinText.toIntOrNull() ?: 0
            val fat = fatText.toIntOrNull() ?: 0

            val userId = intent?.getLongExtra("USER_ID", 0L) ?: 0L

            val meal = Meal(
                userId = userId,
                name = name,
                calories = calories,
                carbs = carbs,
                protein = protein,
                fat = fat,
                type = type
            )

            lifecycleScope.launch {
                val db = AppDatabase.getInstance(applicationContext)
                val dao = db.mealDao()
                withContext(Dispatchers.IO) { dao.insertMeal(meal) }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddMealActivity, getString(R.string.toast_meal_saved), Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }
}
