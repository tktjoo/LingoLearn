package com.linguaflow.app.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.linguaflow.app.domain.model.SpeechEvaluation
import com.linguaflow.app.ui.screens.home.HomeScreen
import com.linguaflow.app.ui.screens.practice.PracticeMenuScreen
import com.linguaflow.app.ui.screens.practice.FlashcardScreen
import com.linguaflow.app.ui.screens.practice.SpeechPracticeScreen
import com.linguaflow.app.ui.screens.practice.SentenceBuildScreen
import com.linguaflow.app.ui.screens.profile.ProfileScreen
import com.linguaflow.app.ui.screens.roleplay.RoleplayScreen
import com.linguaflow.app.ui.screens.roleplay.RoleplayViewModel
import com.linguaflow.app.ui.screens.roleplay.ScenarioPickerScreen
import com.linguaflow.app.ui.screens.speech.SpeechResultScreen
import com.linguaflow.app.ui.screens.streak.StreakScreen
import com.linguaflow.app.ui.screens.vocabulary.AddVocabularyScreen
import com.linguaflow.app.ui.screens.auth.LoginScreen
import com.linguaflow.app.ui.screens.auth.RegisterScreen
import com.linguaflow.app.ui.screens.auth.OtpScreen
import com.linguaflow.app.ui.screens.auth.OnboardingLanguageScreen
import com.linguaflow.app.ui.screens.vocabulary.VocabularyDetailScreen
import com.linguaflow.app.ui.screens.vocabulary.VocabularyListScreen
import com.linguaflow.app.ui.screens.vocabulary.VocabularyViewModel
import com.linguaflow.app.ui.screens.speech.SpeechViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToOtp = {
                    navController.navigate(Screen.Otp.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToOtp = {
                    navController.navigate(Screen.Otp.route)
                }
            )
        }
        composable(Screen.Otp.route) {
            OtpScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Screen.OnboardingLanguage.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.OnboardingLanguage.route) {
            OnboardingLanguageScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OnboardingLanguage.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToPractice = { navController.navigate(Screen.Practice.route) },
                onNavigateToAddVocabulary = { navController.navigate(Screen.AddVocabulary.route) }
            )
        }
        composable(Screen.Vocabulary.route) {
            VocabularyListScreen(
                onNavigateToAdd = { navController.navigate(Screen.AddVocabulary.route) },
                onNavigateToDetail = { wordId -> navController.navigate(Screen.VocabularyDetail.createRoute(wordId)) }
            )
        }
        composable(
            route = Screen.VocabularyDetail.route,
            arguments = listOf(navArgument("wordId") { type = NavType.LongType })
        ) { backStackEntry ->
            val wordId = backStackEntry.arguments?.getLong("wordId") ?: return@composable

            // Use parent entry to share ViewModel
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(Screen.Vocabulary.route)
                } catch (e: Exception) {
                    backStackEntry
                }
            }
            val viewModel: VocabularyViewModel = hiltViewModel(parentEntry)

            VocabularyDetailScreen(
                wordId = wordId,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
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
                onNavigateToRoleplay = { navController.navigate(Screen.ScenarioPicker.route) },
                onNavigateToFlashcards = { navController.navigate(Screen.Flashcards.route) },
                onNavigateToSentenceBuilder = { navController.navigate(Screen.SentenceBuilder.route) }
            )
        }
        composable(Screen.SentenceBuilder.route) {
            SentenceBuildScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ScenarioPicker.route) {
            ScenarioPickerScreen(
                onNavigateBack = { navController.popBackStack() },
                onScenarioSelected = { title, context ->
                    val encodedTitle = java.net.URLEncoder.encode(title, "UTF-8")
                    val encodedContext = java.net.URLEncoder.encode(context, "UTF-8")
                    navController.navigate(Screen.Roleplay.createRoute(encodedTitle, encodedContext))
                }
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
        composable(
            route = Screen.Roleplay.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("context") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "Roleplay", "UTF-8")
            val context = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("context") ?: "", "UTF-8")

            // We pass the context to the ViewModel programmatically here or via SavedStateHandle.
            // For simplicity, we just pass the title as parameter to Screen and let VM read StateHandle.
            // Since we need to update VM, we pass it via a setter method.
            val viewModel: RoleplayViewModel = hiltViewModel()
            androidx.compose.runtime.LaunchedEffect(context) {
                viewModel.initializeScenario(context)
            }

            RoleplayScreen(
                title = title,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
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
