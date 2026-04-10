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
import com.linguaflow.app.ui.screens.practice.SpeechPracticeScreen
import com.linguaflow.app.ui.screens.profile.ProfileScreen
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
            HomeScreen()
        }
        composable(Screen.Vocabulary.route) {
            VocabularyListScreen(
                onNavigateToAdd = { navController.navigate(Screen.AddVocabulary.route) }
            )
        }
        composable(Screen.AddVocabulary.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Vocabulary.route)
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
                onNavigateToSpeechPractice = { navController.navigate(Screen.SpeechPractice.route) }
            )
        }
        composable(Screen.SpeechPractice.route) {
            // A common pattern to share the evaluation result between the practice and result screen
            // is to scope the ViewModel to the parent graph or pass the result via a shared data store/ViewModel.
            // For simplicity in this scaffold, we'll keep the SpeechViewModel at this route, but to pass
            // the complex data object to the next route we will use a shared ViewModel approach.
            val viewModel: SpeechViewModel = hiltViewModel()

            SpeechPracticeScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { evaluation ->
                    // In a real app, passing complex objects through Compose Navigation routes directly isn't recommended.
                    // You would typically save it to a repository/SharedViewModel and read it on the next screen.
                    // For scaffolding purposes, we navigate to the result route and the result screen will observe
                    // the shared ViewModel state.
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

            // Cast to ensure we have a result. If not, fallback or pop stack.
            if (uiState is com.linguaflow.app.ui.screens.speech.SpeechUiState.Result) {
                SpeechResultScreen(
                    evaluation = uiState.evaluation,
                    onNavigateBack = {
                        viewModel.resetState()
                        navController.popBackStack(Screen.Practice.route, false)
                    }
                )
            } else {
                // Fallback if accessed incorrectly
                navController.popBackStack()
            }
        }
        composable(Screen.Streak.route) {
            StreakScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
