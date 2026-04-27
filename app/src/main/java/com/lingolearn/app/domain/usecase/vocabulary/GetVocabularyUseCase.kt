package com.lingolearn.app.domain.usecase.vocabulary

import com.lingolearn.app.data.local.db.entity.VocabularyEntity
import com.lingolearn.app.domain.repository.VocabularyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVocabularyUseCase @Inject constructor(
    private val repository: VocabularyRepository
) {
    operator fun invoke(): Flow<List<VocabularyEntity>> {
        return repository.getAllVocabulary()
    }
}
