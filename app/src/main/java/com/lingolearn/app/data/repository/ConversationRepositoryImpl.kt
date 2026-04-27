package com.lingolearn.app.data.repository

import com.lingolearn.app.data.remote.openai.OpenAIService
import com.lingolearn.app.domain.repository.ConversationRepository
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService
) : ConversationRepository {
    override suspend fun sendMessage(systemContext: String, userMessage: String): String {
        return openAIService.chatWithAI(systemContext, userMessage)
    }
}
