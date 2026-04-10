package com.linguaflow.app.domain.repository

interface ConversationRepository {
    suspend fun sendMessage(systemContext: String, userMessage: String): String
}
