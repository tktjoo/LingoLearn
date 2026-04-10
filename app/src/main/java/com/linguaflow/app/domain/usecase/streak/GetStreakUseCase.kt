package com.linguaflow.app.domain.usecase.streak

import com.linguaflow.app.data.local.db.entity.StreakEntity
import com.linguaflow.app.domain.repository.StreakRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStreakUseCase @Inject constructor(
    private val repository: StreakRepository
) {
    operator fun invoke(): Flow<StreakEntity?> {
        return repository.getStreak()
    }
}
