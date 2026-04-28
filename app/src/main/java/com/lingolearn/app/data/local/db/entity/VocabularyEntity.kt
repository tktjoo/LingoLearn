package com.lingolearn.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary")
data class VocabularyEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val word: String = "",
    val translation: String = "",
    val language: String = "",
    val phonetic: String? = null,
    val exampleSentence: String? = null,
    val category: String? = null,
    val difficultyLevel: Int = 1,
    val timesReviewed: Int = 0,
    val timesCorrect: Int = 0,
    val nextReviewDate: Long = 0L,
    val createdAt: Long = 0L,
    val isFavorite: Boolean = false
)
