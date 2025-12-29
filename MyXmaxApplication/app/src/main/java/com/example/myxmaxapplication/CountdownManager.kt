package com.example.myxmaxapplication

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

data class CountdownData(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
)

object CountdownManager {
    private fun getNextChristmasMillis(nowMillis: Long): Long {
        val now = Calendar.getInstance()
        now.timeInMillis = nowMillis
        val year = now.get(Calendar.YEAR)
        val target = Calendar.getInstance()
        target.clear()
        target.set(year, Calendar.DECEMBER, 25, 0, 0, 0)
        target.set(Calendar.MILLISECOND, 0)
        if (now.timeInMillis >= target.timeInMillis) {
            target.set(Calendar.YEAR, year + 1)
        }
        return target.timeInMillis
    }

    fun ticker(): Flow<CountdownData> = flow {
        while (true) {
            val nowMillis = System.currentTimeMillis()
            val targetMillis = getNextChristmasMillis(nowMillis)
            var diff = targetMillis - nowMillis
            if (diff < 0) diff = 0
            val secondsTotal = diff / 1000
            val days = secondsTotal / (24 * 3600)
            val hours = (secondsTotal % (24 * 3600)) / 3600
            val minutes = (secondsTotal % 3600) / 60
            val seconds = secondsTotal % 60
            emit(CountdownData(days, hours, minutes, seconds))
            delay(1000L)
        }
    }
}
