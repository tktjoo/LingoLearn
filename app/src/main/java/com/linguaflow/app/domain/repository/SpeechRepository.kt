package com.linguaflow.app.domain.repository

import com.linguaflow.app.domain.model.SpeechEvaluation

interface SpeechRepository {
    suspend fun evaluateSpeech(referenceText: String, language: String): SpeechEvaluation?
}
