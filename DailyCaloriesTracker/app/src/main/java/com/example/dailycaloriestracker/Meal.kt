package com.example.dailycaloriestracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meals",
    indices = [Index(value = ["user_id"]), Index(value = ["eaten_at"]) ]
)
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long = 0,
    val name: String,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val type: MealType = MealType.OTHER,
    @ColumnInfo(name = "eaten_at") val eatenAt: Long = System.currentTimeMillis()
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    OTHER
}
