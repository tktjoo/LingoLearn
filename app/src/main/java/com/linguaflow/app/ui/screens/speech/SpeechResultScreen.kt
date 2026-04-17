package com.linguaflow.app.ui.screens.speech

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.linguaflow.app.domain.model.SpeechEvaluation
import com.linguaflow.app.ui.components.ScoreRadialChart
import com.linguaflow.app.ui.theme.ErrorRed
import com.linguaflow.app.ui.theme.SuccessGreen
import com.linguaflow.app.ui.theme.WarningYellow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SpeechResultScreen(
    evaluation: SpeechEvaluation,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados da Avaliação") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Overall Score
            Text(
                text = "Pontuação Global",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            ScoreRadialChart(
                score = evaluation.overallScore,
                label = "",
                size = 120.dp,
                strokeWidth = 12.dp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sub-scores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreRadialChart(score = evaluation.accuracyScore, label = "Precisão")
                ScoreRadialChart(score = evaluation.fluencyScore, label = "Fluência")
                ScoreRadialChart(score = evaluation.prosodyScore, label = "Prosódia")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Word Breakdown
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Detalhes por Palavra",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val annotatedText = buildAnnotatedString {
                        evaluation.wordDetails.forEach { wordAssessment ->
                            val color = when {
                                wordAssessment.errorType == "Omission" -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                wordAssessment.errorType == "Insertion" -> ErrorRed.copy(alpha = 0.7f)
                                wordAssessment.accuracyScore >= 80 -> SuccessGreen
                                wordAssessment.accuracyScore >= 60 -> WarningYellow
                                else -> ErrorRed
                            }

                            val isStrikethrough = wordAssessment.errorType == "Omission"

                            withStyle(
                                style = SpanStyle(
                                    color = color,
                                    textDecoration = if (isStrikethrough) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                                    background = if (wordAssessment.errorType == "Insertion") ErrorRed.copy(alpha=0.1f) else Color.Transparent
                                )
                            ) {
                                append(wordAssessment.word + " ")
                            }
                        }
                    }

                    Text(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar")
            }
        }
    }
}
