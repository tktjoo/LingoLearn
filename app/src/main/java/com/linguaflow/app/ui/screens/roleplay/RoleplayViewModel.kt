package com.linguaflow.app.ui.screens.roleplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.domain.model.ConversationMessage
import com.linguaflow.app.domain.model.SenderType
import com.linguaflow.app.data.local.datastore.UserPreferences
import com.linguaflow.app.domain.usecase.conversation.ChatWithAIUseCase
import com.linguaflow.app.domain.usecase.speech.RecognizeSpeechUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RoleplayViewModel @Inject constructor(
    private val chatWithAIUseCase: ChatWithAIUseCase,
    private val recognizeSpeechUseCase: RecognizeSpeechUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var systemContext: String = ""

    private val _messages = MutableStateFlow<List<ConversationMessage>>(emptyList())
    val messages: StateFlow<List<ConversationMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private var isInitialized = false

    fun initializeScenario(context: String) {
        if (isInitialized) return
        systemContext = context
        isInitialized = true
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
            val targetLanguage = userPreferences.targetLanguageFlow.first()
            val azureLocale = mapLanguageCodeToAzureLocale(targetLanguage)

            val recognizedText = recognizeSpeechUseCase(language = azureLocale)
            _isRecording.value = false

            if (!recognizedText.isNullOrBlank()) {
                sendMessage(recognizedText)
            }
        }
    }

    private fun mapLanguageCodeToAzureLocale(code: String): String {
        return when (code) {
            "en" -> "en-US"
            "es" -> "es-ES"
            "fr" -> "fr-FR"
            "de" -> "de-DE"
            "it" -> "it-IT"
            "nl" -> "nl-NL"
            "ko" -> "ko-KR"
            "ja" -> "ja-JP"
            "zh" -> "zh-CN"
            "vi" -> "vi-VN"
            "hi" -> "hi-IN"
            "ar" -> "ar-SA"
            "th" -> "th-TH"
            else -> "en-US"
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
