package com.linguaflow.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.linguaflow.app.ui.theme.PrimaryBlue

@Composable
fun LoginScreen(
    onNavigateToOtp: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val otpSent by viewModel.otpSent.collectAsState()

    LaunchedEffect(otpSent) { if (otpSent) onNavigateToOtp() }

    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("LingoLearn", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            Spacer(Modifier.height(48.dp))
            if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 16.dp))
            OutlinedTextField(value = email, onValueChange = { email = it; viewModel.clearError() }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = { password = it; viewModel.clearError() }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(Modifier.height(32.dp))
            Button(onClick = { viewModel.startOtpProcess(email, "Utilizador") }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), enabled = !isLoading) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp)) else Text("Entrar")
            }
            TextButton(onClick = { onNavigateToRegister() }) { Text("Criar conta", color = PrimaryBlue) }
        }
    }
}