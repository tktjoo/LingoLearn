package com.linguaflow.app.data.repository

import com.linguaflow.app.data.remote.openai.OpenAIService
import com.linguaflow.app.domain.repository.ConversationRepository
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService
) : ConversationRepository {
    override suspend fun sendMessage(systemContext: String, userMessage: String): String {
        return openAIService.chatWithAI(systemContext, userMessage)
    }
}
