package com.example.caloriestracker

data class Meal(
    val id: String = "",
    val name: String = "",
    val calories: Int = 0,
    val fats: Int = 0,
    val proteins: Int = 0,
    val carbs: Int = 0,
    val type: MealType = MealType.BREAKFAST
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}