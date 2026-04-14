package com.linguaflow.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Início", Icons.Default.Home, Screen.Home.route),
        BottomNavItem("Vocabulário", Icons.Default.List, Screen.Vocabulary.route),
        BottomNavItem("Praticar", Icons.Default.PlayArrow, Screen.Practice.route),
        BottomNavItem("Estatísticas", Icons.Default.DateRange, Screen.Streak.route),
        BottomNavItem("Perfil", Icons.Default.Person, Screen.Profile.route)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Map sub-routes to their parent bottom nav item route for highlighting
        val isSelected = { itemRoute: String ->
            when (itemRoute) {
                Screen.Vocabulary.route -> currentRoute == Screen.Vocabulary.route || currentRoute == Screen.AddVocabulary.route
                Screen.Practice.route -> currentRoute == Screen.Practice.route || currentRoute == Screen.SpeechPractice.route || currentRoute == Screen.SpeechResult.route || currentRoute == Screen.Roleplay.route
                else -> currentRoute == itemRoute
            }
        }

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = isSelected(item.route),
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}
