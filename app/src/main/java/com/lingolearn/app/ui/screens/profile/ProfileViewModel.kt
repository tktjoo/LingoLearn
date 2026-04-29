package com.lingolearn.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingolearn.app.data.local.datastore.UserPreferences
import com.lingolearn.app.data.local.db.entity.StreakEntity
import com.lingolearn.app.domain.usecase.streak.GetStreakUseCase
import com.lingolearn.app.domain.usecase.vocabulary.GetVocabularyUseCase
import com.lingolearn.app.utils.ReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getStreakUseCase: GetStreakUseCase,
    getVocabularyUseCase: GetVocabularyUseCase,
    private val userPreferences: UserPreferences,
    private val reminderManager: ReminderManager
) : ViewModel() {

    init {
        viewModelScope.launch {
            val isEnabled = userPreferences.notificationsFlow.first()
            if (isEnabled) {
                reminderManager.scheduleReminder()
            } else {
                reminderManager.cancelReminder()
            }
        }
    }

    val streak: StateFlow<StreakEntity?> = getStreakUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val totalWordsLearned: StateFlow<Int> = getVocabularyUseCase()
        .map { words -> words.size }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val darkThemeEnabled: StateFlow<Boolean> = userPreferences.darkThemeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val notificationsEnabled: StateFlow<Boolean> = userPreferences.notificationsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val dailyGoal: StateFlow<Int> = userPreferences.dailyGoalFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 50
        )

    val targetLanguage: StateFlow<String> = userPreferences.targetLanguageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    val userName: StateFlow<String> = userPreferences.userNameFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val userEmail: StateFlow<String> = userPreferences.userEmailFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setDarkTheme(enabled)
        }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setNotifications(enabled)
            if (enabled) {
                reminderManager.scheduleReminder()
            } else {
                reminderManager.cancelReminder()
            }
        }
    }

    fun setDailyGoal(goal: Int) {
        viewModelScope.launch {
            userPreferences.setDailyGoal(goal)
        }
    }

    fun setTargetLanguage(languageCode: String) {
        viewModelScope.launch {
            userPreferences.setTargetLanguage(languageCode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.setLoggedIn(false)
        }
    }
}
