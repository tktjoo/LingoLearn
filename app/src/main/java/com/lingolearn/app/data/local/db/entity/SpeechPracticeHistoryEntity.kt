package com.lingolearn.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speech_history")
data class SpeechPracticeHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val referenceText: String,
    val recognizedText: String,
    val overallScore: Float,
    val accuracyScore: Float,
    val fluencyScore: Float,
    val timestamp: Long,
    val languageCode: String
)
