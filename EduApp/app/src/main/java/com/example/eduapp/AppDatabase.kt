package com.example.eduapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [User::class, Question::class, Result::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun resultDao(): ResultDao

    companion object {
        @kotlin.jvm.Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "eduapp.db"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                // Pre-populate 10 simple questions
                                val now = System.currentTimeMillis()

                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('What is the capital of France?', 'Paris', 'Berlin', 'Rome', 'Madrid', 0, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('Which planet is known as the Red Planet?', 'Venus', 'Mars', 'Jupiter', 'Saturn', 1, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('What is 2 + 2?', '3', '4', '5', '6', 1, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('Who wrote \"Romeo and Juliet\"?', 'Charles Dickens', 'William Shakespeare', 'Leo Tolstoy', 'Mark Twain', 1, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('What color do you get by mixing red and white?', 'Pink', 'Green', 'Purple', 'Orange', 0, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('Which animal is known as the King of the Jungle?', 'Elephant', 'Tiger', 'Lion', 'Giraffe', 2, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('What is the largest ocean on Earth?', 'Atlantic', 'Indian', 'Arctic', 'Pacific', 3, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('How many continents are there?', '5', '6', '7', '8', 2, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('Which gas do plants mainly absorb?', 'Oxygen', 'Nitrogen', 'Carbon Dioxide', 'Hydrogen', 2, '', $now)")
                                db.execSQL("INSERT INTO questions (text, answer_a, answer_b, answer_c, answer_d, correct_index, explanation, created_at) VALUES ('What is the boiling point of water at sea level in Celsius?', '90', '100', '110', '120', 1, '', $now)")
                            }
                        }).build()

                    INSTANCE = instance
                }
                return INSTANCE!!
            }
        }
    }
}
