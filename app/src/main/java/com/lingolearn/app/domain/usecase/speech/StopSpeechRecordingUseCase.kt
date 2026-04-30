package com.lingolearn.app.domain.usecase.speech

import com.lingolearn.app.domain.repository.SpeechRepository
import javax.inject.Inject

class StopSpeechRecordingUseCase @Inject constructor(
    private val repository: SpeechRepository
) {
    operator fun invoke() {
        repository.stopRecording()
    }
}
