package com.linguaflow.app.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.linguaflow.app.domain.model.SpeechEvaluation
import com.linguaflow.app.ui.screens.home.HomeScreen
import com.linguaflow.app.ui.screens.practice.PracticeMenuScreen
import com.linguaflow.app.ui.screens.practice.FlashcardScreen
import com.linguaflow.app.ui.screens.practice.SpeechPracticeScreen
import com.linguaflow.app.ui.screens.profile.ProfileScreen
import com.linguaflow.app.ui.screens.roleplay.RoleplayScreen
import com.linguaflow.app.ui.screens.speech.SpeechResultScreen
import com.linguaflow.app.ui.screens.streak.StreakScreen
import com.linguaflow.app.ui.screens.vocabulary.AddVocabularyScreen
import com.linguaflow.app.ui.screens.vocabulary.VocabularyListScreen
import com.linguaflow.app.ui.screens.vocabulary.VocabularyViewModel
import com.linguaflow.app.ui.screens.speech.SpeechViewModel

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
            HomeScreen(
                onNavigateToPractice = { navController.navigate(Screen.Practice.route) },
                onNavigateToAddVocabulary = { navController.navigate(Screen.AddVocabulary.route) }
            )
        }
        composable(Screen.Vocabulary.route) {
            VocabularyListScreen(
                onNavigateToAdd = { navController.navigate(Screen.AddVocabulary.route) }
            )
        }
        composable(Screen.AddVocabulary.route) { backStackEntry ->
            // Try to get parent entry if we navigated from Vocabulary list.
            // If we navigated directly from Home, parentEntry might not exist,
            // so we fallback to the current entry.
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(Screen.Vocabulary.route)
                } catch (e: Exception) {
                    backStackEntry
                }
            }
            val viewModel: VocabularyViewModel = hiltViewModel(parentEntry)

            AddVocabularyScreen(
                onNavigateBack = { navController.popBackStack() },
                onSave = { word, translation, language, category ->
                    viewModel.addVocabulary(word, translation, language, category)
                }
            )
        }
        composable(Screen.Practice.route) {
            PracticeMenuScreen(
                onNavigateToSpeechPractice = { navController.navigate(Screen.SpeechPractice.route) },
                onNavigateToRoleplay = { navController.navigate(Screen.Roleplay.route) },
                onNavigateToFlashcards = { navController.navigate(Screen.Flashcards.route) }
            )
        }
        composable(Screen.Flashcards.route) {
            FlashcardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SpeechPractice.route) {
            val viewModel: SpeechViewModel = hiltViewModel()

            SpeechPracticeScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { evaluation ->
                    navController.navigate(Screen.SpeechResult.route)
                },
                viewModel = viewModel
            )
        }
        composable(Screen.SpeechResult.route) { backStackEntry ->
             val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.SpeechPractice.route)
            }
            val viewModel: SpeechViewModel = hiltViewModel(parentEntry)
            val uiState = viewModel.uiState.value

            if (uiState is com.linguaflow.app.ui.screens.speech.SpeechUiState.Result) {
                SpeechResultScreen(
                    evaluation = uiState.evaluation,
                    onNavigateBack = {
                        navController.popBackStack(Screen.Practice.route, false)
                        viewModel.resetState()
                    }
                )
            } else {
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
        composable(Screen.Roleplay.route) {
            RoleplayScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Streak.route) {
            StreakScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
