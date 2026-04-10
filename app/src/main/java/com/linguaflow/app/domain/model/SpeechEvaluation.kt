package com.linguaflow.app.domain.model

data class SpeechEvaluation(
    val overallScore: Float,
    val accuracyScore: Float,
    val fluencyScore: Float,
    val prosodyScore: Float,
    val completenessScore: Float,
    val grammarScore: Float,
    val vocabularyScore: Float,
    val topicScore: Float,
    val recognizedText: String,
    val referenceText: String?,
    val wordDetails: List<WordAssessment>,
    val timestamp: Long
)

data class WordAssessment(
    val word: String,
    val accuracyScore: Float,
    val errorType: String?
)
