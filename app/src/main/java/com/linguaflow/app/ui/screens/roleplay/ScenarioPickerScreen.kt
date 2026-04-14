package com.linguaflow.app.ui.screens.roleplay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Scenario(
    val id: String,
    val title: String,
    val description: String,
    val systemContext: String,
    val emoji: String
)

val defaultScenarios = listOf(
    Scenario(
        id = "restaurant",
        title = "No Restaurante",
        description = "Pratica pedir comida, pedir recomendações e pagar a conta.",
        systemContext = "You are a friendly waiter at a traditional restaurant. The user is a customer who is learning the language. You should speak only in the target language, keep your sentences relatively short, and be helpful. Start by greeting the customer and asking for their order.",
        emoji = "🍽️"
    ),
    Scenario(
        id = "airport",
        title = "Check-in no Aeroporto",
        description = "Despacha as tuas malas, mostra o passaporte e encontra a porta de embarque.",
        systemContext = "You are an airline check-in agent at an international airport. The user is a passenger. Speak only in the target language. Keep sentences short. Start by asking for their passport and ticket.",
        emoji = "✈️"
    ),
    Scenario(
        id = "interview",
        title = "Entrevista de Emprego",
        description = "Pratica vocabulário profissional numa entrevista formal.",
        systemContext = "You are a hiring manager interviewing the user for a software engineering position. Speak only in the target language using formal tone. Start by welcoming them and asking them to introduce themselves.",
        emoji = "💼"
    ),
    Scenario(
        id = "directions",
        title = "Pedir Indicações",
        description = "Perdido na cidade? Pede ajuda a um local para chegar à estação de comboios.",
        systemContext = "You are a local resident walking down the street. The user is a tourist asking for directions. Speak only in the target language. Be helpful but natural. Start by saying 'Hello, how can I help you?'.",
        emoji = "🗺️"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenarioPickerScreen(
    onNavigateBack: () -> Unit,
    onScenarioSelected: (String, String) -> Unit // title, systemContext
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escolhe um Cenário") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(defaultScenarios, key = { it.id }) { scenario ->
                ScenarioCard(scenario = scenario, onClick = {
                    onScenarioSelected(scenario.title, scenario.systemContext)
                })
            }
        }
    }
}

@Composable
fun ScenarioCard(scenario: Scenario, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = scenario.emoji,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(
                    text = scenario.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = scenario.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
