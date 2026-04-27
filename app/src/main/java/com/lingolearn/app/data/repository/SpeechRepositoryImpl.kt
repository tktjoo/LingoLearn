package com.lingolearn.app.data.repository

import com.lingolearn.app.data.remote.azure.AzureSpeechService
import com.lingolearn.app.domain.model.SpeechEvaluation
import com.lingolearn.app.domain.repository.SpeechRepository
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

    override suspend fun recognizeSpeech(language: String): String? {
        return azureSpeechService.recognizeSpeech(language)
    }
}
