package com.lingolearn.app.domain.model

data class ConversationMessage(
    val id: String,
    val sender: SenderType,
    val text: String,
    val timestamp: Long
)

enum class SenderType {
    USER, AI, SYSTEM
}
