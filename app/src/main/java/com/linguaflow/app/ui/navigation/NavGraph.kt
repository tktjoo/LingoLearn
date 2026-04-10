package com.linguaflow.app.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.linguaflow.app.ui.screens.home.HomeScreen
import com.linguaflow.app.ui.screens.practice.PracticeMenuScreen
import com.linguaflow.app.ui.screens.profile.ProfileScreen
import com.linguaflow.app.ui.screens.streak.StreakScreen
import com.linguaflow.app.ui.screens.vocabulary.VocabularyListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Vocabulary.route) {
            VocabularyListScreen()
        }
        composable(Screen.Practice.route) {
            PracticeMenuScreen()
        }
        composable(Screen.Streak.route) {
            StreakScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
