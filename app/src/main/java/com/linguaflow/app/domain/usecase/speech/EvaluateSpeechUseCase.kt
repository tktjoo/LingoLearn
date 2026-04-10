package com.linguaflow.app.domain.usecase.speech

import com.linguaflow.app.domain.model.SpeechEvaluation
import com.linguaflow.app.domain.repository.SpeechRepository
import javax.inject.Inject

class EvaluateSpeechUseCase @Inject constructor(
    private val repository: SpeechRepository
) {
    suspend operator fun invoke(referenceText: String, language: String): SpeechEvaluation? {
        return repository.evaluateSpeech(referenceText, language)
    }
}
