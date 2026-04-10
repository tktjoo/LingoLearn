package com.linguaflow.app.ui.screens.speech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.domain.model.SpeechEvaluation
import com.linguaflow.app.domain.usecase.speech.EvaluateSpeechUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val evaluateSpeechUseCase: EvaluateSpeechUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SpeechUiState>(SpeechUiState.Idle)
    val uiState: StateFlow<SpeechUiState> = _uiState.asStateFlow()

    fun evaluateSpeech(referenceText: String, language: String = "en-US") {
        _uiState.value = SpeechUiState.Recording

        viewModelScope.launch {
            // Note: The Azure SDK recognition is currently blocking in the data layer wrapper.
            // We set to "Evaluating" once the SDK call starts processing the payload.
            _uiState.value = SpeechUiState.Evaluating

            val result = evaluateSpeechUseCase(referenceText, language)

            if (result != null) {
                _uiState.value = SpeechUiState.Result(result)
            } else {
                _uiState.value = SpeechUiState.Error("Failed to evaluate speech. Please try again.")
            }
        }
    }

    fun resetState() {
        _uiState.value = SpeechUiState.Idle
    }
}
