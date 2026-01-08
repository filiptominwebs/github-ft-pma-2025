package com.example.dailycaloriestracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): User?

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun countByEmail(email: String): Int

    // Authenticate by comparing stored password_hash with the provided passwordHash
    @Query("SELECT * FROM users WHERE email = :email AND password_hash = :passwordHash LIMIT 1")
    suspend fun authenticateByEmail(email: String, passwordHash: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND password_hash = :passwordHash LIMIT 1")
    suspend fun authenticateByUsername(username: String, passwordHash: String): User?
}