package com.lingolearn.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lingolearn.app.data.local.datastore.UserPreferences
import com.lingolearn.app.ui.navigation.BottomNavBar
import com.lingolearn.app.ui.navigation.NavGraph
import com.lingolearn.app.ui.theme.LinguaFlowTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkThemeEnabled by userPreferences.darkThemeFlow.collectAsState(initial = false)
            val isLoggedIn by userPreferences.isLoggedInFlow.collectAsState(initial = null)
            val hasCompletedOnboarding by userPreferences.hasCompletedOnboardingFlow.collectAsState(initial = null)

            LinguaFlowTheme(darkTheme = darkThemeEnabled) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    if (isLoggedIn != null && hasCompletedOnboarding != null) {
                        val startDestination = when {
                            isLoggedIn == false -> com.lingolearn.app.ui.navigation.Screen.AuthRoute.route
                            hasCompletedOnboarding == false -> com.lingolearn.app.ui.navigation.Screen.OnboardingLanguage.route
                            else -> com.lingolearn.app.ui.navigation.Screen.Home.route
                        }

                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                        val showBottomNav = currentRoute in listOf(
                            com.lingolearn.app.ui.navigation.Screen.Home.route,
                            com.lingolearn.app.ui.navigation.Screen.Vocabulary.route,
                            com.lingolearn.app.ui.navigation.Screen.Practice.route,
                            com.lingolearn.app.ui.navigation.Screen.Streak.route,
                            com.lingolearn.app.ui.navigation.Screen.Profile.route
                        )

                        Scaffold(
                            bottomBar = {
                                if (showBottomNav) {
                                    BottomNavBar(navController = navController)
                                }
                            }
                        ) { innerPadding ->
                            NavGraph(
                                navController = navController,
                                paddingValues = innerPadding,
                                startDestination = startDestination
                            )
                        }
                    }
                }
            }
        }
    }
}
