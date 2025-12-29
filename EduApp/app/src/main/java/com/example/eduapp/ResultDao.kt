package com.example.eduapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result): Long

    @Query("SELECT * FROM results WHERE user_id = :userId ORDER BY created_at DESC")
    suspend fun getResultsForUser(userId: Long): List<Result>

    @Query("DELETE FROM results WHERE user_id = :userId")
    suspend fun deleteResultsForUser(userId: Long)

}

