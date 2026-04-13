package com.linguaflow.app.ui.screens.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.local.db.entity.VocabularyEntity
import com.linguaflow.app.domain.usecase.streak.GetStreakUseCase
import com.linguaflow.app.domain.usecase.streak.UpdateStreakUseCase
import com.linguaflow.app.domain.usecase.vocabulary.GetVocabularyUseCase
import com.linguaflow.app.domain.usecase.vocabulary.UpdateVocabularyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FlashcardUiState {
    object Loading : FlashcardUiState()
    object Empty : FlashcardUiState()
    data class Active(val currentWord: VocabularyEntity, val currentIndex: Int, val totalCount: Int) : FlashcardUiState()
    object Finished : FlashcardUiState()
}

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val getVocabularyUseCase: GetVocabularyUseCase,
    private val updateVocabularyUseCase: UpdateVocabularyUseCase,
    private val getStreakUseCase: GetStreakUseCase,
    private val updateStreakUseCase: UpdateStreakUseCase
) : ViewModel() {

    private val _flashcardUiState = MutableStateFlow<FlashcardUiState>(FlashcardUiState.Loading)
    val flashcardUiState: StateFlow<FlashcardUiState> = _flashcardUiState

    private var wordsList: List<VocabularyEntity> = emptyList()
    private var currentIndex = 0

    init {
        loadWords()
    }

    private fun loadWords() {
        viewModelScope.launch {
            val words = getVocabularyUseCase().first()
            if (words.isEmpty()) {
                _flashcardUiState.value = FlashcardUiState.Empty
            } else {
                // In a real app, we would sort by NextReviewDate.
                // For this demo, we'll just shuffle them.
                wordsList = words.shuffled()
                currentIndex = 0
                showCurrentWord()
            }
        }
    }

    private fun showCurrentWord() {
        if (currentIndex < wordsList.size) {
            _flashcardUiState.value = FlashcardUiState.Active(
                currentWord = wordsList[currentIndex],
                currentIndex = currentIndex,
                totalCount = wordsList.size
            )
        } else {
            _flashcardUiState.value = FlashcardUiState.Finished
        }
    }

    fun submitResult(remembered: Boolean) {
        viewModelScope.launch {
            val currentWord = wordsList[currentIndex]

            // Simple SRS placeholder logic:
            // If remembered, increase timesCorrect and set next review date further.
            // If not, reset timesCorrect.
            val updatedWord = if (remembered) {
                currentWord.copy(
                    timesReviewed = currentWord.timesReviewed + 1,
                    timesCorrect = currentWord.timesCorrect + 1,
                    nextReviewDate = System.currentTimeMillis() + (86400000L * 2) // +2 days
                )
            } else {
                currentWord.copy(
                    timesReviewed = currentWord.timesReviewed + 1,
                    timesCorrect = 0,
                    nextReviewDate = System.currentTimeMillis() + 3600000L // +1 hour
                )
            }

            updateVocabularyUseCase(updatedWord)

            // Add XP for practicing (e.g., 5 XP per word)
            awardXP(5)

            // Move to next word
            currentIndex++
            showCurrentWord()
        }
    }

    private suspend fun awardXP(amount: Int) {
        val currentStreak = getStreakUseCase().firstOrNull() ?: return
        updateStreakUseCase(
            currentStreak.copy(
                dailyXP = currentStreak.dailyXP + amount,
                totalXP = currentStreak.totalXP + amount,
                lastPracticeDate = System.currentTimeMillis()
            )
        )
    }
}
