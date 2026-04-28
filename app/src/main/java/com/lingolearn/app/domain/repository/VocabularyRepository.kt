package com.lingolearn.app.domain.repository

import com.lingolearn.app.data.local.db.entity.VocabularyEntity
import kotlinx.coroutines.flow.Flow

interface VocabularyRepository {
    fun getAllVocabulary(): Flow<List<VocabularyEntity>>
    suspend fun insertVocabulary(vocabulary: VocabularyEntity): String
    suspend fun updateVocabulary(vocabulary: VocabularyEntity)
    suspend fun deleteVocabulary(vocabulary: VocabularyEntity)
}
