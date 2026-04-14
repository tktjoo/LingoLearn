package com.linguaflow.app.ui.screens.practice

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.linguaflow.app.data.local.db.entity.VocabularyEntity
import com.linguaflow.app.ui.screens.vocabulary.VocabularyViewModel
import com.linguaflow.app.ui.theme.ErrorRed
import com.linguaflow.app.ui.theme.PrimaryBlue
import com.linguaflow.app.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    onNavigateBack: () -> Unit,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.flashcardUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flashcards", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val state = uiState) {
                is FlashcardUiState.Loading -> {
                    Text("A carregar flashcards...")
                }
                is FlashcardUiState.Empty -> {
                    Text(
                        text = "Ainda não tens palavras de vocabulário para praticar neste idioma. Adiciona algumas palavras primeiro!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                is FlashcardUiState.Finished -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Prática Concluída!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Reviste todas as tuas palavras.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = onNavigateBack,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Text("Voltar ao Menu")
                        }
                    }
                }
                is FlashcardUiState.Active -> {
                    FlashcardView(
                        vocabulary = state.currentWord,
                        currentIndex = state.currentIndex,
                        totalCount = state.totalCount,
                        onResult = { remembered -> viewModel.submitResult(remembered) }
                    )
                }
            }
        }
    }
}

@Composable
fun FlashcardView(
    vocabulary: VocabularyEntity,
    currentIndex: Int,
    totalCount: Int,
    onResult: (Boolean) -> Unit
) {
    var flipped by remember(vocabulary) { mutableStateOf(false) }

    val rotationY by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "flipAnimation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Palavra ${currentIndex + 1} de $totalCount",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // The Flippable Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .graphicsLayer {
                    this.rotationY = rotationY
                    cameraDistance = 12f * density
                }
                .clickable { flipped = !flipped },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (rotationY <= 90f) {
                    // Front of card (Word)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = vocabulary.word,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toca para virar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    // Back of card (Translation)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.graphicsLayer { this.rotationY = 180f } // Fix mirrored text
                    ) {
                        Text(
                            text = vocabulary.translation,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        if (!vocabulary.category.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Categoria: ${vocabulary.category}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons (Only show when flipped)
        if (flipped) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onResult(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Não sabia")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Não Sabia")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { onResult(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Sabia")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sabia")
                }
            }
        } else {
             Spacer(modifier = Modifier.height(56.dp)) // Placeholder to prevent jumping
        }
    }
}
