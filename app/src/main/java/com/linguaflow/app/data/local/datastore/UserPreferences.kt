package com.linguaflow.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    val darkThemeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DARK_THEME] ?: false
    }

    val notificationsFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
    }

    val dailyGoalFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DAILY_GOAL_XP] ?: 50
    }

    val targetLanguageFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.TARGET_LANGUAGE] ?: "en" // Default to English
    }

    val isLoggedInFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
    }

    val hasCompletedOnboardingFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] ?: false
    }

    val userNameFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME] ?: "Utilizador"
    }

    val userEmailFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_EMAIL] ?: ""
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = enabled
        }
    }

    suspend fun setNotifications(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setDailyGoal(xp: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_GOAL_XP] = xp
        }
    }

    suspend fun setTargetLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TARGET_LANGUAGE] = languageCode
        }
    }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun setHasCompletedOnboarding(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] = completed
        }
    }

    suspend fun setUserProfile(name: String, email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
            preferences[PreferencesKeys.USER_EMAIL] = email
        }
    }

    private object PreferencesKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val DAILY_GOAL_XP = intPreferencesKey("daily_goal_xp")
        val TARGET_LANGUAGE = stringPreferencesKey("target_language")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }
}
