package com.linguaflow.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streaks")
data class StreakEntity(
    @PrimaryKey val id: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastPracticeDate: Long,
    val totalDaysPracticed: Int = 0,
    val weeklyGoal: Int = 7,
    val dailyXP: Int = 0,
    val totalXP: Long = 0
)
