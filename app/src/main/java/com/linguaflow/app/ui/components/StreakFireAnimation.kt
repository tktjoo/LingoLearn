package com.linguaflow.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.linguaflow.app.ui.theme.WarningYellow

@Composable
fun StreakFireAnimation(
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    // Scaffold implementation for Lottie Animation placeholder.
    // In a real application, you would load a Lottie composition here using LottieAnimation
    // and select the variant (small, medium, large) based on the currentStreak.

    val iconColor = if (currentStreak > 0) WarningYellow else Color.Gray

    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "Streak Fire",
        tint = iconColor,
        modifier = modifier.size(64.dp)
    )
}
