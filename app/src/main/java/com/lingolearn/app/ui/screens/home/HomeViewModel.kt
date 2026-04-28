package com.lingolearn.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingolearn.app.data.local.db.entity.StreakEntity
import com.lingolearn.app.domain.usecase.streak.GetStreakUseCase
import com.lingolearn.app.domain.usecase.streak.UpdateStreakUseCase
import com.lingolearn.app.domain.usecase.streak.InsertStreakUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getStreakUseCase: GetStreakUseCase,
    private val updateStreakUseCase: UpdateStreakUseCase,
    private val insertStreakUseCase: InsertStreakUseCase
) : ViewModel() {

    // Expose the streak as a StateFlow for the UI to observe
    val streak: StateFlow<StreakEntity?> = getStreakUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        // Initialize a default streak if none exists (for demo purposes)
        viewModelScope.launch {
            val currentStreak = getStreakUseCase().firstOrNull()
            if (currentStreak == null) {
                insertStreakUseCase(
                    StreakEntity(
                        id = "default",
                        currentStreak = 0,
                        longestStreak = 0,
                        lastPracticeDate = System.currentTimeMillis(),
                        totalDaysPracticed = 0,
                        weeklyGoal = 7,
                        dailyXP = 0,
                        totalXP = 0L
                    )
                )
            }
        }
    }
}
