package com.lingolearn.app.data.repository

import com.lingolearn.app.data.local.db.dao.SpeechHistoryDao
import com.lingolearn.app.data.local.db.entity.SpeechPracticeHistoryEntity
import com.lingolearn.app.data.remote.azure.AzureSpeechService
import com.lingolearn.app.domain.model.SpeechEvaluation
import com.lingolearn.app.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpeechRepositoryImpl @Inject constructor(
    private val azureSpeechService: AzureSpeechService,
    private val speechHistoryDao: SpeechHistoryDao
) : SpeechRepository {
    override suspend fun evaluateSpeech(
        referenceText: String,
        language: String
    ): SpeechEvaluation {
        return azureSpeechService.evaluatePronunciation(referenceText, language)
    }

    override suspend fun recognizeSpeech(language: String): String? {
        return azureSpeechService.recognizeSpeech(language)
    }

    override suspend fun saveSpeechEvaluation(evaluation: SpeechPracticeHistoryEntity) {
        speechHistoryDao.insertSpeechEvaluation(evaluation)
    }

    override fun getSpeechHistory(): Flow<List<SpeechPracticeHistoryEntity>> {
        return speechHistoryDao.getAllSpeechHistory()
    }

    override fun stopRecording() {
        azureSpeechService.stopRecording()
    }
}
