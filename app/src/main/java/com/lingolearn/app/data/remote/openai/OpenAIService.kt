package com.lingolearn.app.data.remote.openai

import com.lingolearn.app.data.remote.openai.model.ChatCompletionRequest
import com.lingolearn.app.data.remote.openai.model.ChatMessage
import javax.inject.Inject

class OpenAIService @Inject constructor(
    private val openAIApi: OpenAIApi
) {
    suspend fun chatWithAI(systemPrompt: String, userMessage: String): String {
        return try {
            val messages = listOf(
                ChatMessage(role = "system", content = systemPrompt),
                ChatMessage(role = "user", content = userMessage)
            )
            val request = ChatCompletionRequest(messages = messages)
            val response = openAIApi.getChatCompletion(request)

            response.choices.firstOrNull()?.message?.content ?: "Desculpe, não consegui obter uma resposta."
        } catch (e: Exception) {
            "Erro de comunicação com a IA: ${e.message}"
        }
    }
}
