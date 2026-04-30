package com.lingolearn.app.domain.usecase.speech

import com.lingolearn.app.data.local.db.entity.SpeechPracticeHistoryEntity
import com.lingolearn.app.domain.repository.SpeechRepository
import javax.inject.Inject

class SaveSpeechEvaluationUseCase @Inject constructor(
    private val repository: SpeechRepository
) {
    suspend operator fun invoke(evaluation: SpeechPracticeHistoryEntity) {
        repository.saveSpeechEvaluation(evaluation)
    }
}
