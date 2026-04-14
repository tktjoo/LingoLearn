package com.linguaflow.app.ui.screens.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.linguaflow.app.ui.theme.ErrorRed
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.linguaflow.app.domain.model.SentenceExercise
import com.linguaflow.app.domain.model.globalSentenceExercises
import com.linguaflow.app.ui.theme.PrimaryBlue
import com.linguaflow.app.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SentenceBuildScreen(
    onNavigateBack: () -> Unit,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    // Filter exercises based on current selected learning language
    val languageExercises = remember(currentLanguage) {
        globalSentenceExercises.filter { it.languageCode == currentLanguage }
    }

    if (languageExercises.isEmpty()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Construir a Frase") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    text = "Ainda não existem exercícios para esta língua.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    var currentExerciseIndex by remember(currentLanguage) { mutableStateOf(0) }
    val exercise = languageExercises[currentExerciseIndex]

    var selectedWords by remember(exercise) { mutableStateOf(listOf<String>()) }
    var availableWords by remember(exercise) { mutableStateOf(exercise.words.shuffled()) }

    var showResult by remember(currentExerciseIndex) { mutableStateOf(false) }
    var isCorrect by remember(currentExerciseIndex) { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Construir a Frase") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Traduz esta frase:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = exercise.translation,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Construction Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedWords.forEach { word ->
                        WordChip(word = word, isSelected = true) {
                            if (!showResult) {
                                selectedWords = selectedWords - word
                                availableWords = availableWords + word
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Available Words Area
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                availableWords.forEach { word ->
                    WordChip(word = word, isSelected = false) {
                        if (!showResult) {
                            availableWords = availableWords - word
                            selectedWords = selectedWords + word
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (showResult) {
                val color = if (isCorrect) SuccessGreen else ErrorRed
                val msg = if (isCorrect) "Correto!" else "Incorreto. A resposta é: ${exercise.targetSentence}"

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color.copy(alpha = 0.2f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(msg, color = color, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    if (showResult) {
                        if (currentExerciseIndex < languageExercises.size - 1) {
                            currentExerciseIndex++
                        } else {
                            onNavigateBack() // Completed all exercises
                        }
                    } else {
                        val currentSentence = selectedWords.joinToString(" ")
                        isCorrect = currentSentence.trim() == exercise.targetSentence.trim()
                        showResult = true
                        if (isCorrect) {
                            viewModel.addXpForSentencePractice()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedWords.isNotEmpty() || showResult,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showResult && !isCorrect) ErrorRed else PrimaryBlue
                )
            ) {
                Text(if (showResult) "Continuar" else "Verificar Resposta", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun WordChip(word: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = word,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}
