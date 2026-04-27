package com.lingolearn.app.domain.usecase.streak

import com.lingolearn.app.data.local.db.entity.StreakEntity
import com.lingolearn.app.domain.repository.StreakRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar
import javax.inject.Inject

class AddXpAndCheckStreakUseCase @Inject constructor(
    private val repository: StreakRepository
) {
    suspend operator fun invoke(xpToAdd: Int) {
        val currentStreak = repository.getStreak().firstOrNull() ?: return

        val now = Calendar.getInstance()
        val lastPractice = Calendar.getInstance().apply {
            timeInMillis = currentStreak.lastPracticeDate
        }

        val isSameDay = now.get(Calendar.YEAR) == lastPractice.get(Calendar.YEAR) &&
                        now.get(Calendar.DAY_OF_YEAR) == lastPractice.get(Calendar.DAY_OF_YEAR)

        val isNextDay = now.get(Calendar.YEAR) == lastPractice.get(Calendar.YEAR) &&
                        now.get(Calendar.DAY_OF_YEAR) == lastPractice.get(Calendar.DAY_OF_YEAR) + 1 ||
                        (now.get(Calendar.YEAR) == lastPractice.get(Calendar.YEAR) + 1 && lastPractice.get(Calendar.DAY_OF_YEAR) >= 365 && now.get(Calendar.DAY_OF_YEAR) == 1)

        val newStreakCount = when {
            isSameDay -> currentStreak.currentStreak
            isNextDay -> currentStreak.currentStreak + 1
            else -> 1 // Reset streak if more than 1 day has passed
        }

        val newLongestStreak = maxOf(currentStreak.longestStreak, newStreakCount)

        val newTotalDays = if (!isSameDay) currentStreak.totalDaysPracticed + 1 else currentStreak.totalDaysPracticed
        val newDailyXp = if (isSameDay) currentStreak.dailyXP + xpToAdd else xpToAdd

        val updatedStreak = currentStreak.copy(
            currentStreak = newStreakCount,
            longestStreak = newLongestStreak,
            totalDaysPracticed = newTotalDays,
            dailyXP = newDailyXp,
            totalXP = currentStreak.totalXP + xpToAdd,
            lastPracticeDate = now.timeInMillis
        )

        repository.updateStreak(updatedStreak)
    }
}
