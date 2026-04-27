package com.lingolearn.app.domain.usecase.conversation

import com.lingolearn.app.domain.repository.ConversationRepository
import javax.inject.Inject

class ChatWithAIUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke(systemContext: String, userMessage: String): String {
        return repository.sendMessage(systemContext, userMessage)
    }
}
