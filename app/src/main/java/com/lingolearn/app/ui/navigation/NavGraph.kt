package com.lingolearn.app.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.lingolearn.app.ui.screens.home.HomeScreen
import com.lingolearn.app.ui.screens.practice.*
import com.lingolearn.app.ui.screens.profile.ProfileScreen
import com.lingolearn.app.ui.screens.roleplay.*
import com.lingolearn.app.ui.screens.speech.*
import com.lingolearn.app.ui.screens.streak.StreakScreen
import com.lingolearn.app.ui.screens.vocabulary.*
import com.lingolearn.app.ui.screens.auth.*

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
        navigation(startDestination = Screen.Login.route, route = Screen.AuthRoute.route) {
            composable(Screen.Login.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.AuthRoute.route)
                }
                val viewModel: AuthViewModel = hiltViewModel(parentEntry)
                LoginScreen(
                    onNavigateToOtp = { navController.navigate(Screen.Otp.route) },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    viewModel = viewModel
                )
            }
            composable(Screen.Register.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.AuthRoute.route)
                }
                val viewModel: AuthViewModel = hiltViewModel(parentEntry)
                RegisterScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToOtp = { navController.navigate(Screen.Otp.route) },
                    viewModel = viewModel
                )
            }
            composable(Screen.Otp.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.AuthRoute.route)
                }
                val viewModel: AuthViewModel = hiltViewModel(parentEntry)
                OtpScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.AuthRoute.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
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
            arguments = listOf(navArgument("wordId") { type = NavType.StringType })
        ) { backStackEntry ->
            val wordId = backStackEntry.arguments?.getString("wordId") ?: return@composable
            val parentEntry = remember(backStackEntry) {
                try { navController.getBackStackEntry(Screen.Vocabulary.route) }
                catch (_: Exception) { backStackEntry }
            }
            val viewModel: VocabularyViewModel = hiltViewModel(parentEntry)
            VocabularyDetailScreen(
                wordId = wordId,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable(Screen.AddVocabulary.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                try { navController.getBackStackEntry(Screen.Vocabulary.route) }
                catch (_: Exception) { backStackEntry }
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
            SentenceBuildScreen(onNavigateBack = { navController.popBackStack() })
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
            FlashcardScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.SpeechPractice.route) {
            val viewModel: SpeechViewModel = hiltViewModel()
            SpeechPracticeScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToResult = { navController.navigate(Screen.SpeechResult.route) },
                viewModel = viewModel
            )
        }
        composable(Screen.SpeechResult.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.SpeechPractice.route)
            }
            val viewModel: SpeechViewModel = hiltViewModel(parentEntry)

            // Observação reativa corrigida
            val uiState by viewModel.uiState.collectAsState()

            if (uiState is SpeechUiState.Result) {
                SpeechResultScreen(
                    evaluation = (uiState as SpeechUiState.Result).evaluation,
                    onNavigateBack = {
                        navController.popBackStack(Screen.Practice.route, false)
                        viewModel.resetState()
                    }
                )
            } else {
                LaunchedEffect(Unit) { navController.popBackStack() }
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
            val viewModel: RoleplayViewModel = hiltViewModel()
            LaunchedEffect(context) { viewModel.initializeScenario(context) }
            RoleplayScreen(title = title, onNavigateBack = { navController.popBackStack() }, viewModel = viewModel)
        }
        composable(Screen.Streak.route) { StreakScreen() }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}