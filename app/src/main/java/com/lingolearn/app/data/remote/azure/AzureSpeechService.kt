package com.lingolearn.app.data.remote.azure

import android.content.Context
import android.util.Log
import com.lingolearn.app.domain.model.SpeechEvaluation
import com.lingolearn.app.domain.model.WordAssessment
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentConfig
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechRecognizer
import com.microsoft.cognitiveservices.speech.ResultReason
import com.microsoft.cognitiveservices.speech.CancellationDetails
import com.microsoft.cognitiveservices.speech.PropertyId
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

    private val subscriptionKey = com.lingolearn.app.BuildConfig.AZURE_SPEECH_KEY
    private val serviceRegion = com.lingolearn.app.BuildConfig.AZURE_SPEECH_REGION

    private var activeRecognizer: SpeechRecognizer? = null

    fun stopRecording() {
        try {
            activeRecognizer?.stopContinuousRecognitionAsync()
            activeRecognizer?.close()
        } catch (e: Exception) {
            Log.e("AzureSpeechService", "Error stopping recording", e)
        } finally {
            activeRecognizer = null
        }
    }

    suspend fun recognizeSpeech(language: String = "en-US"): String? = withContext(Dispatchers.IO) {
        var speechConfig: SpeechConfig? = null
        var audioConfig: AudioConfig? = null
        var recognizer: SpeechRecognizer? = null
        try {
            speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion)
            speechConfig.speechRecognitionLanguage = language

            speechConfig.setProperty(PropertyId.SpeechServiceConnection_InitialSilenceTimeoutMs, "3000")
            speechConfig.setProperty(PropertyId.SpeechServiceConnection_EndSilenceTimeoutMs, "2000")

            audioConfig = AudioConfig.fromDefaultMicrophoneInput()
            recognizer = SpeechRecognizer(speechConfig, audioConfig)

            activeRecognizer = recognizer

            val result = recognizer.recognizeOnceAsync().get()
            return@withContext result.text
        } catch (e: Exception) {
            Log.e("AzureSpeechService", "Error recognizing speech", e)
            null
        } finally {
            activeRecognizer = null
            recognizer?.close()
            audioConfig?.close()
            speechConfig?.close()
        }
    }

    suspend fun evaluatePronunciation(
        referenceText: String,
        language: String = "en-US"
    ): SpeechEvaluation = withContext(Dispatchers.IO) {
        var speechConfig: SpeechConfig? = null
        var audioConfig: AudioConfig? = null
        var recognizer: SpeechRecognizer? = null
        var pronunciationConfig: PronunciationAssessmentConfig? = null
        try {
            speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion)
            speechConfig.speechRecognitionLanguage = language

            speechConfig.setProperty(PropertyId.SpeechServiceConnection_InitialSilenceTimeoutMs, "3000")
            speechConfig.setProperty(PropertyId.SpeechServiceConnection_EndSilenceTimeoutMs, "2000")

            audioConfig = AudioConfig.fromDefaultMicrophoneInput()
            recognizer = SpeechRecognizer(speechConfig, audioConfig)

            pronunciationConfig = PronunciationAssessmentConfig(
                referenceText,
                PronunciationAssessmentGradingSystem.HundredMark,
                PronunciationAssessmentGranularity.Word,
                true
            )
            pronunciationConfig.applyTo(recognizer)

            activeRecognizer = recognizer

            // Blocks and waits for a single utterance
            val result = recognizer.recognizeOnceAsync().get()

            if (result.reason == ResultReason.Canceled) {
                val cancellation = CancellationDetails.fromResult(result)
                throw RuntimeException("Erro: ${cancellation.errorDetails}")
            } else if (result.reason == ResultReason.NoMatch) {
                throw RuntimeException("Nenhuma fala detectada. Tenta falar de novo.")
            }

            val pronunciationResult = PronunciationAssessmentResult.fromResult(result)

            val evaluation = if (pronunciationResult != null) {
                val words = pronunciationResult.words.map { word ->
                    WordAssessment(
                        word = word.word,
                        accuracyScore = ScoreHelper.getWordAccuracyScore(word),
                        errorType = word.errorType
                    )
                }

                SpeechEvaluation(
                    overallScore = ScoreHelper.getPronunciationScore(pronunciationResult),
                    accuracyScore = ScoreHelper.getAccuracyScore(pronunciationResult),
                    fluencyScore = ScoreHelper.getFluencyScore(pronunciationResult),
                    prosodyScore = ScoreHelper.getProsodyScore(pronunciationResult),
                    completenessScore = ScoreHelper.getCompletenessScore(pronunciationResult),
                    grammarScore = 0f, // Content assessment requires additional config
                    vocabularyScore = 0f, // Content assessment requires additional config
                    topicScore = 0f, // Content assessment requires additional config
                    recognizedText = result.text,
                    referenceText = referenceText,
                    wordDetails = words,
                    timestamp = System.currentTimeMillis()
                )
            } else {
                throw RuntimeException("Falha ao obter resultados da pronúncia.")
            }

            return@withContext evaluation
        } catch (e: Exception) {
            Log.e("AzureSpeechService", "Error evaluating speech", e)
            throw e
        } finally {
            activeRecognizer = null
            recognizer?.close()
            audioConfig?.close()
            speechConfig?.close()
            pronunciationConfig?.close()
        }
    }
}
