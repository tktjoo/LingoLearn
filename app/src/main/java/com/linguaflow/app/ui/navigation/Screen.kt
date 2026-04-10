package com.linguaflow.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Vocabulary : Screen("vocabulary")
    object Practice : Screen("practice")
    object Streak : Screen("streak")
    object Profile : Screen("profile")
}
