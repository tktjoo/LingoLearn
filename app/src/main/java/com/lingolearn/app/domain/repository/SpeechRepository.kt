package com.lingolearn.app.domain.repository

import com.lingolearn.app.domain.model.SpeechEvaluation
import com.lingolearn.app.data.local.db.entity.SpeechPracticeHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SpeechRepository {
    suspend fun evaluateSpeech(referenceText: String, language: String): SpeechEvaluation?
    suspend fun recognizeSpeech(language: String): String?
    suspend fun saveSpeechEvaluation(evaluation: SpeechPracticeHistoryEntity)
    fun getSpeechHistory(): Flow<List<SpeechPracticeHistoryEntity>>
}
