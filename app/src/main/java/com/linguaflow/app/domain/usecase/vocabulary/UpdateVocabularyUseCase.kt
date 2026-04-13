package com.linguaflow.app.domain.usecase.vocabulary

import com.linguaflow.app.data.local.db.entity.VocabularyEntity
import com.linguaflow.app.domain.repository.VocabularyRepository
import javax.inject.Inject

class UpdateVocabularyUseCase @Inject constructor(
    private val repository: VocabularyRepository
) {
    suspend operator fun invoke(vocabulary: VocabularyEntity) {
        repository.updateVocabulary(vocabulary)
    }
}
