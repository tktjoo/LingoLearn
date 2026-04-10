package com.linguaflow.app.data.repository

import com.linguaflow.app.data.local.db.dao.StreakDao
import com.linguaflow.app.data.local.db.entity.StreakEntity
import com.linguaflow.app.domain.repository.StreakRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StreakRepositoryImpl @Inject constructor(
    private val streakDao: StreakDao
) : StreakRepository {
    override fun getStreak(): Flow<StreakEntity?> {
        return streakDao.getStreak()
    }

    override suspend fun insertStreak(streak: StreakEntity) {
        streakDao.insertStreak(streak)
    }

    override suspend fun updateStreak(streak: StreakEntity) {
        streakDao.updateStreak(streak)
    }
}
