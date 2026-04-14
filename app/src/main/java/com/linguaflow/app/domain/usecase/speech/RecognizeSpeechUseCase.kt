package com.linguaflow.app.domain.usecase.speech

import com.linguaflow.app.domain.repository.SpeechRepository
import javax.inject.Inject

class RecognizeSpeechUseCase @Inject constructor(
    private val repository: SpeechRepository
) {
    suspend operator fun invoke(language: String): String? {
        return repository.recognizeSpeech(language)
    }
}
