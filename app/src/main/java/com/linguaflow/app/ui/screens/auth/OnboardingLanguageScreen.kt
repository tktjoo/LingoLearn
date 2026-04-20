package com.linguaflow.app.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.linguaflow.app.domain.model.SupportedLanguages
import com.linguaflow.app.ui.theme.PrimaryBlue

@Composable
fun OnboardingLanguageScreen(
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var selectedLang by remember { mutableStateOf<String?>(null) }

    // Acesso corrigido via fluxo público no ViewModel
    val hasCompletedOnboarding by viewModel.hasCompletedOnboardingFlow.collectAsState(initial = false)

    LaunchedEffect(hasCompletedOnboarding) {
        if (hasCompletedOnboarding) {
            onNavigateToHome()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Que língua queres aprender?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(SupportedLanguages) { (code, name) ->
                    LanguageCard(
                        name = name,
                        isSelected = selectedLang == code,
                        onClick = { selectedLang = code }
                    )
                }
            }

            Button(
                onClick = { selectedLang?.let { viewModel.completeOnboarding(it) } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = selectedLang != null
            ) {
                Text("Começar a Aprender", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun LanguageCard(name: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) PrimaryBlue.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (isSelected) PrimaryBlue else Color.Transparent

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) Icon(Icons.Default.Check, "Selected", tint = PrimaryBlue)
        }
    }
}