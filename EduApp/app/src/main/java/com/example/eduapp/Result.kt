package com.example.eduapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class Result(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "total") val total: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)

