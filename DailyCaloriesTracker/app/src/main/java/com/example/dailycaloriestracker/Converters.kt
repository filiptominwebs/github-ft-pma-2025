package com.example.dailycaloriestracker

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromMealType(type: MealType?): String? {
        return type?.name
    }

    @TypeConverter
    fun toMealType(value: String?): MealType? {
        return value?.let { MealType.valueOf(it) }
    }
}

