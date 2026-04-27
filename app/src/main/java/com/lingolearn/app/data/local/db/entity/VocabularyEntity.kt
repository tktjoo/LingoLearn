package com.lingolearn.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary")
data class VocabularyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val translation: String,
    val language: String,
    val phonetic: String?,
    val exampleSentence: String?,
    val category: String?,
    val difficultyLevel: Int,
    val timesReviewed: Int = 0,
    val timesCorrect: Int = 0,
    val nextReviewDate: Long,
    val createdAt: Long,
    val isFavorite: Boolean = false
)
