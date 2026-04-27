package com.lingolearn.app.domain.repository

import com.lingolearn.app.domain.model.SpeechEvaluation

interface SpeechRepository {
    suspend fun evaluateSpeech(referenceText: String, language: String): SpeechEvaluation?
    suspend fun recognizeSpeech(language: String): String?
}
