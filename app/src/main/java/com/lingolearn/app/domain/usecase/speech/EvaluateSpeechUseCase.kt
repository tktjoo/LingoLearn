package com.lingolearn.app.domain.usecase.speech

import com.lingolearn.app.domain.model.SpeechEvaluation
import com.lingolearn.app.domain.repository.SpeechRepository
import javax.inject.Inject

class EvaluateSpeechUseCase @Inject constructor(
    private val repository: SpeechRepository
) {
    suspend operator fun invoke(referenceText: String, language: String): SpeechEvaluation {
        return repository.evaluateSpeech(referenceText, language)
    }
}
