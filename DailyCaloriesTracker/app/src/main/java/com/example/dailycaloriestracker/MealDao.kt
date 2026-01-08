package com.example.dailycaloriestracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMeal(meal: Meal): Long

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM meals WHERE id = :id LIMIT 1")
    suspend fun getMealById(id: Long): Meal?

    @Query("SELECT * FROM meals WHERE user_id = :userId ORDER BY eaten_at DESC")
    suspend fun getMealsForUser(userId: Long): List<Meal>

    @Query("SELECT * FROM meals WHERE user_id = :userId AND eaten_at BETWEEN :startMs AND :endMs ORDER BY eaten_at DESC")
    suspend fun getMealsForUserBetween(userId: Long, startMs: Long, endMs: Long): List<Meal>

    @Query("SELECT COUNT(*) FROM meals WHERE user_id = :userId")
    suspend fun countMealsForUser(userId: Long): Int
}

