package com.example.eduapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "answer_a") val answerA: String,
    @ColumnInfo(name = "answer_b") val answerB: String,
    @ColumnInfo(name = "answer_c") val answerC: String,
    @ColumnInfo(name = "answer_d") val answerD: String,
    // correctIndex is 0..3 corresponding to answerA..answerD
    @ColumnInfo(name = "correct_index") val correctIndex: Int,
    @ColumnInfo(name = "explanation") val explanation: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
