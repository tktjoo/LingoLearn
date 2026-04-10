package com.linguaflow.app.domain.repository

import com.linguaflow.app.data.local.db.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

interface StreakRepository {
    fun getStreak(): Flow<StreakEntity?>
    suspend fun insertStreak(streak: StreakEntity)
    suspend fun updateStreak(streak: StreakEntity)
}
