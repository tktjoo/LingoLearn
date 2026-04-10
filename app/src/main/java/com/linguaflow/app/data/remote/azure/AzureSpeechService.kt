package com.linguaflow.app.data.remote.azure

import android.content.Context
import android.util.Log
import com.linguaflow.app.domain.model.SpeechEvaluation
import com.linguaflow.app.domain.model.WordAssessment
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentConfig
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechRecognizer
import com.microsoft.cognitiveservices.speech.audio.AudioConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AzureSpeechService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Note: API Keys should ideally be injected securely (e.g., BuildConfig)
    private val subscriptionKey = "YOUR_AZURE_SUBSCRIPTION_KEY"
    private val serviceRegion = "YOUR_AZURE_REGION"

    suspend fun evaluatePronunciation(
        referenceText: String,
        language: String = "en-US"
    ): SpeechEvaluation? = withContext(Dispatchers.IO) {
        try {
            val speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion)
            speechConfig.speechRecognitionLanguage = language

            val audioConfig = AudioConfig.fromDefaultMicrophoneInput()
            val recognizer = SpeechRecognizer(speechConfig, audioConfig)

            val pronunciationConfig = PronunciationAssessmentConfig(
                referenceText,
                PronunciationAssessmentGradingSystem.HundredMark,
                PronunciationAssessmentGranularity.Word,
                true
            )
            pronunciationConfig.applyTo(recognizer)

            // Blocks and waits for a single utterance
            val result = recognizer.recognizeOnceAsync().get()

            val pronunciationResult = PronunciationAssessmentResult.fromResult(result)

            val evaluation = if (pronunciationResult != null) {
                val words = pronunciationResult.words.map { word ->
                    WordAssessment(
                        word = word.word,
                        accuracyScore = word.accuracyScore.toFloat(),
                        errorType = word.errorType
                    )
                }

                SpeechEvaluation(
                    overallScore = pronunciationResult.pronunciationScore.toFloat(),
                    accuracyScore = pronunciationResult.accuracyScore.toFloat(),
                    fluencyScore = pronunciationResult.fluencyScore.toFloat(),
                    prosodyScore = pronunciationResult.prosodyScore.toFloat(),
                    completenessScore = pronunciationResult.completenessScore.toFloat(),
                    grammarScore = 0f, // Content assessment requires additional config
                    vocabularyScore = 0f, // Content assessment requires additional config
                    topicScore = 0f, // Content assessment requires additional config
                    recognizedText = result.text,
                    referenceText = referenceText,
                    wordDetails = words,
                    timestamp = System.currentTimeMillis()
                )
            } else null

            recognizer.close()
            audioConfig.close()
            speechConfig.close()
            pronunciationConfig.close()

            return@withContext evaluation
        } catch (e: Exception) {
            Log.e("AzureSpeechService", "Error evaluating speech", e)
            null
        }
    }
}
