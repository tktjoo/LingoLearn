package com.lingolearn.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lingolearn.app.ui.theme.SuccessGreen
import com.lingolearn.app.ui.theme.WarningYellow
import com.lingolearn.app.ui.theme.ErrorRed

@Composable
fun ScoreRadialChart(
    score: Float, // 0 to 100
    label: String,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    strokeWidth: Dp = 8.dp
) {
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    val animatedScore by animateFloatAsState(
        targetValue = if (animationPlayed) score else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = FastOutSlowInEasing
        ),
        label = "Score Animation"
    )

    val color = when {
        score >= 80 -> SuccessGreen
        score >= 60 -> WarningYellow
        else -> ErrorRed
    }

    val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
            Canvas(modifier = Modifier.size(size)) {
                // Draw background track
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )

                // Draw score arc
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = (animatedScore / 100f) * 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }

            Text(
                text = "${animatedScore.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
