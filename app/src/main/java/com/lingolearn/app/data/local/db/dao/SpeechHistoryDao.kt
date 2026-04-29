package com.lingolearn.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lingolearn.app.data.local.db.entity.SpeechPracticeHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeechHistoryDao {
    @Query("SELECT * FROM speech_history ORDER BY timestamp DESC")
    fun getAllSpeechHistory(): Flow<List<SpeechPracticeHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeechEvaluation(evaluation: SpeechPracticeHistoryEntity)
}
