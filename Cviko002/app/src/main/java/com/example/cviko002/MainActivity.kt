package com.example.cviko002

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
        setContentView(R.layout.activity_main)

        val etMealName = findViewById<EditText>(R.id.etMealName)
        val etCalories = findViewById<EditText>(R.id.etCalories)
        val etMealType = findViewById<EditText>(R.id.etMealType)
        val btAddMeal = findViewById<Button>(R.id.btAddMeal)
        val btClearMeals = findViewById<Button>(R.id.btClearMeals)
        val tvMeals = findViewById<TextView>(R.id.tvMeals)

        btAddMeal.setOnClickListener {
            val mealName = etMealName.text.toString()
            val calories = etCalories.text.toString()
            val mealType = etMealType.text.toString()

            val mealEntry = "Meal: $mealName, Calories: $calories, Type: $mealType\n"
            tvMeals.append(mealEntry)
        }

        btClearMeals.setOnClickListener {
            tvMeals.text = ""
        }


    }
}