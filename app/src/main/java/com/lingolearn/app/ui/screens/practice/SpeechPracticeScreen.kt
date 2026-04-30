package com.lingolearn.app.ui.screens.practice

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.lingolearn.app.domain.model.SpeechEvaluation
import com.lingolearn.app.ui.screens.speech.SpeechUiState
import com.lingolearn.app.ui.screens.speech.SpeechViewModel
import com.lingolearn.app.ui.theme.ErrorRed

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SpeechPracticeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResult: (SpeechEvaluation) -> Unit,
    viewModel: SpeechViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val referenceText by viewModel.currentReferenceText.collectAsState()
    val recordAudioPermission = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    LaunchedEffect(uiState) {
        if (uiState is SpeechUiState.Result) {
            onNavigateToResult((uiState as SpeechUiState.Result).evaluation)
            // State reset is managed by the Result Screen when the user explicitly navigates away
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prática de Pronúncia") },
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
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Por favor lê o seguinte texto em voz alta:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = referenceText,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            when (uiState) {
                is SpeechUiState.Idle, is SpeechUiState.Error -> {
                    if (uiState is SpeechUiState.Error) {
                        Text(
                            text = (uiState as SpeechUiState.Error).message,
                            color = ErrorRed,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    RecordButton(
                        isRecording = false,
                        onClick = {
                            if (recordAudioPermission.status.isGranted) {
                                viewModel.evaluateSpeech()
                            } else {
                                recordAudioPermission.launchPermissionRequest()
                            }
                        }
                    )
                }
                is SpeechUiState.Recording, is SpeechUiState.Evaluating -> {
                    if (uiState is SpeechUiState.Evaluating) {
                        CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
                        Text("A avaliar a tua pronúncia...")
                    } else {
                        RecordButton(
                            isRecording = true,
                            onClick = { viewModel.stopRecording() }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("A gravar... Toca para parar", color = ErrorRed)
                    }
                }
                is SpeechUiState.Result -> {
                    // Handled by LaunchedEffect
                }
            }
        }
    }
}

@Composable
fun RecordButton(isRecording: Boolean, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRecording) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(80.dp)
            .scale(scale)
            .background(
                color = if (isRecording) ErrorRed.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            )
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (isRecording) ErrorRed else MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for a proper Mic icon. Using a simple color box for now if not available
             Text("MIC", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
