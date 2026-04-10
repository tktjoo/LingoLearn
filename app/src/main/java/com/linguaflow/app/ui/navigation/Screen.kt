package com.linguaflow.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Vocabulary : Screen("vocabulary")
    object AddVocabulary : Screen("add_vocabulary")
    object Practice : Screen("practice")
    object SpeechPractice : Screen("speech_practice")
    object SpeechResult : Screen("speech_result")
    object Roleplay : Screen("roleplay")
    object Streak : Screen("streak")
    object Profile : Screen("profile")
}
