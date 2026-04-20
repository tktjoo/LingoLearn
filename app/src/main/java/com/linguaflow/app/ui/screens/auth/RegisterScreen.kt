package com.linguaflow.app.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.linguaflow.app.ui.theme.PrimaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToOtp: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val otpSent by viewModel.otpSent.collectAsState()

    LaunchedEffect(otpSent) { if (otpSent) onNavigateToOtp() }

    Scaffold(topBar = { TopAppBar(title = { Text("") }, navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Criar Conta", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            Spacer(Modifier.height(32.dp))
            if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)
            OutlinedTextField(value = name, onValueChange = { name = it; viewModel.clearError() }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it; viewModel.clearError() }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it; viewModel.clearError() }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(32.dp))
            Button(onClick = { viewModel.startOtpProcess(email, name) }, modifier = Modifier.fillMaxWidth().height(56.dp), enabled = !isLoading) {
                Text("Registar")
            }
        }
    }
}