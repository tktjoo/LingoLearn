package com.linguaflow.app.data.repository

import com.linguaflow.app.data.remote.azure.AzureSpeechService
import com.linguaflow.app.domain.model.SpeechEvaluation
import com.linguaflow.app.domain.repository.SpeechRepository
import javax.inject.Inject

class SpeechRepositoryImpl @Inject constructor(
    private val azureSpeechService: AzureSpeechService
) : SpeechRepository {
    override suspend fun evaluateSpeech(
        referenceText: String,
        language: String
    ): SpeechEvaluation? {
        return azureSpeechService.evaluatePronunciation(referenceText, language)
    }
}
