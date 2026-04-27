package com.lingolearn.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lingolearn.app.ui.theme.PrimaryBlue

@Composable
fun OtpScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var otpCode by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val targetEmail by viewModel.targetEmail.collectAsState()

    val isLoggedIn by viewModel.isLoggedInFlow.collectAsState(initial = false)

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onNavigateToHome()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Verificação",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Introduza o código enviado para: ${targetEmail ?: "o seu email"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = otpCode,
                onValueChange = { otpCode = it; viewModel.clearError() },
                label = { Text("Código OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.verifyOtp(otpCode) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading && otpCode.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Verificar")
                }
            }

            TextButton(
                onClick = {
                    viewModel.resetOtpState()
                    onNavigateBack()
                }
            ) {
                Text("Voltar", color = PrimaryBlue)
            }
        }
    }
}