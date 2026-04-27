package com.lingolearn.app.ui.screens.speech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingolearn.app.domain.model.SpeechEvaluation
import com.lingolearn.app.data.local.datastore.UserPreferences
import com.lingolearn.app.domain.model.globalSentenceExercises
import com.lingolearn.app.domain.usecase.speech.EvaluateSpeechUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SpeechUiState {
    object Idle : SpeechUiState()
    object Recording : SpeechUiState()
    object Evaluating : SpeechUiState()
    data class Result(val evaluation: SpeechEvaluation) : SpeechUiState()
    data class Error(val message: String) : SpeechUiState()
}

@HiltViewModel
class SpeechViewModel @Inject constructor(
    private val evaluateSpeechUseCase: EvaluateSpeechUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<SpeechUiState>(SpeechUiState.Idle)
    val uiState: StateFlow<SpeechUiState> = _uiState.asStateFlow()

    private val _currentReferenceText = MutableStateFlow("")
    val currentReferenceText: StateFlow<String> = _currentReferenceText.asStateFlow()

    init {
        loadNewSentence()
    }

    fun loadNewSentence() {
        viewModelScope.launch {
            val targetLanguage = userPreferences.targetLanguageFlow.first()
            val exercises = globalSentenceExercises.filter { it.languageCode == targetLanguage }

            if (exercises.isNotEmpty()) {
                val exercise = exercises.random()
                _currentReferenceText.value = exercise.targetSentence
            } else {
                _currentReferenceText.value = "Sem frases disponíveis para este idioma."
            }
        }
    }

    fun evaluateSpeech() {
        if (_uiState.value is SpeechUiState.Recording) return

        _uiState.value = SpeechUiState.Recording

        viewModelScope.launch {
            val targetLanguage = userPreferences.targetLanguageFlow.first()
            val textToEvaluate = _currentReferenceText.value

            val result = evaluateSpeechUseCase(textToEvaluate, mapLanguageCodeToAzureLocale(targetLanguage))

            _uiState.value = SpeechUiState.Evaluating

            _uiState.value = SpeechUiState.Evaluating

            if (result != null) {
                _uiState.value = SpeechUiState.Result(result)
            } else {
                _uiState.value = SpeechUiState.Error("Falha ao avaliar a fala. Por favor tenta de novo.")
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

    fun resetState() {
        _uiState.value = SpeechUiState.Idle
        loadNewSentence()
    }
}
