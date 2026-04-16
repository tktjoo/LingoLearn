package com.linguaflow.app.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object OnboardingLanguage : Screen("onboarding_language")

    object Home : Screen("home")
    object Vocabulary : Screen("vocabulary")
    object VocabularyDetail : Screen("vocabulary_detail/{wordId}") {
        fun createRoute(wordId: Long) = "vocabulary_detail/$wordId"
    }
    object AddVocabulary : Screen("add_vocabulary")
    object Practice : Screen("practice")
    object Flashcards : Screen("flashcards")
    object SentenceBuilder : Screen("sentence_builder")
    object SpeechPractice : Screen("speech_practice")
    object SpeechResult : Screen("speech_result")
    object ScenarioPicker : Screen("scenario_picker")
    object Roleplay : Screen("roleplay/{title}/{context}") {
        fun createRoute(title: String, context: String) = "roleplay/$title/$context"
    }
    object Streak : Screen("streak")
    object Profile : Screen("profile")
}
