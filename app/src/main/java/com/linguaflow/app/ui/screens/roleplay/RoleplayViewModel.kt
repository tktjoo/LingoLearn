package com.linguaflow.app.ui.screens.roleplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.domain.model.ConversationMessage
import com.linguaflow.app.domain.model.SenderType
import com.linguaflow.app.domain.usecase.conversation.ChatWithAIUseCase
import com.linguaflow.app.domain.usecase.speech.RecognizeSpeechUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RoleplayViewModel @Inject constructor(
    private val chatWithAIUseCase: ChatWithAIUseCase,
    private val recognizeSpeechUseCase: RecognizeSpeechUseCase
) : ViewModel() {

    // A simple context for our AI roleplay scenario
    private val systemContext = """
        You are a friendly waiter at a traditional restaurant in Portugal.
        The user is a customer who is learning Portuguese.
        You should speak only in Portuguese (PT-PT), keep your sentences relatively short,
        and be helpful. Start by greeting the customer.
    """.trimIndent()

    private val _messages = MutableStateFlow<List<ConversationMessage>>(emptyList())
    val messages: StateFlow<List<ConversationMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    init {
        // Automatically start the conversation with the AI greeting
        sendInitialGreeting()
    }

    private fun sendInitialGreeting() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = chatWithAIUseCase(systemContext, "Olá! Pode começar a conversa como se eu acabasse de entrar no restaurante.")
            addMessage(response, SenderType.AI)
            _isLoading.value = false
        }
    }

    fun startRecording() {
        if (_isRecording.value) return

        viewModelScope.launch {
            _isRecording.value = true
            val recognizedText = recognizeSpeechUseCase(language = "pt-PT")
            _isRecording.value = false

            if (!recognizedText.isNullOrBlank()) {
                sendMessage(recognizedText)
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // 1. Add User Message to UI
        addMessage(text, SenderType.USER)

        // 2. Send to AI
        viewModelScope.launch {
            _isLoading.value = true

            // To provide context, we should ideally send the entire conversation history.
            // For this scaffold, we send the system context and the user's latest message.
            // The UseCase currently takes systemContext + userMessage.
            val response = chatWithAIUseCase(systemContext, text)

            // 3. Add AI Response to UI
            addMessage(response, SenderType.AI)
            _isLoading.value = false
        }
    }

    private fun addMessage(text: String, sender: SenderType) {
        val newMessage = ConversationMessage(
            id = UUID.randomUUID().toString(),
            sender = sender,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        _messages.update { currentList ->
            currentList + newMessage
        }
    }
}
