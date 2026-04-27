package com.lingolearn.app.domain.usecase.streak

import com.lingolearn.app.data.local.db.entity.StreakEntity
import com.lingolearn.app.domain.repository.StreakRepository
import javax.inject.Inject

class InsertStreakUseCase @Inject constructor(
    private val repository: StreakRepository
) {
    suspend operator fun invoke(streak: StreakEntity) {
        repository.insertStreak(streak)
    }
}
