package com.lingolearn.app.domain.usecase.vocabulary

import com.lingolearn.app.data.local.db.entity.VocabularyEntity
import com.lingolearn.app.domain.repository.VocabularyRepository
import javax.inject.Inject

class AddVocabularyUseCase @Inject constructor(
    private val repository: VocabularyRepository
) {
    suspend operator fun invoke(vocabulary: VocabularyEntity): Long {
        return repository.insertVocabulary(vocabulary)
    }
}
