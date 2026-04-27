package com.lingolearn.app.data.repository

import com.lingolearn.app.data.local.db.dao.VocabularyDao
import com.lingolearn.app.data.local.db.entity.VocabularyEntity
import com.lingolearn.app.domain.repository.VocabularyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VocabularyRepositoryImpl @Inject constructor(
    private val vocabularyDao: VocabularyDao
) : VocabularyRepository {
    override fun getAllVocabulary(): Flow<List<VocabularyEntity>> {
        return vocabularyDao.getAllVocabulary()
    }

    override suspend fun insertVocabulary(vocabulary: VocabularyEntity): Long {
        return vocabularyDao.insertVocabulary(vocabulary)
    }

    override suspend fun updateVocabulary(vocabulary: VocabularyEntity) {
        vocabularyDao.updateVocabulary(vocabulary)
    }

    override suspend fun deleteVocabulary(vocabulary: VocabularyEntity) {
        vocabularyDao.deleteVocabulary(vocabulary)
    }
}
