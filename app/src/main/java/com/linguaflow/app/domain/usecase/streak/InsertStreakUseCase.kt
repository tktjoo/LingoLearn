package com.linguaflow.app.domain.usecase.streak

import com.linguaflow.app.data.local.db.entity.StreakEntity
import com.linguaflow.app.domain.repository.StreakRepository
import javax.inject.Inject

class InsertStreakUseCase @Inject constructor(
    private val repository: StreakRepository
) {
    suspend operator fun invoke(streak: StreakEntity) {
        repository.insertStreak(streak)
    }
}
