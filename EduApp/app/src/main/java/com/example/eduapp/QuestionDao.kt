package com.example.eduapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT * FROM questions ORDER BY id")
    suspend fun getAllQuestions(): List<Question>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionById(id: Long): Question?

    // Gets a random set of questions (SQLite RANDOM())
    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestions(limit: Int): List<Question>

}
