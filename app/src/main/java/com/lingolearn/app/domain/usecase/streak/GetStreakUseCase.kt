package com.lingolearn.app.domain.usecase.streak

import com.lingolearn.app.data.local.db.entity.StreakEntity
import com.lingolearn.app.domain.repository.StreakRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStreakUseCase @Inject constructor(
    private val repository: StreakRepository
) {
    operator fun invoke(): Flow<StreakEntity?> {
        return repository.getStreak()
    }
}
