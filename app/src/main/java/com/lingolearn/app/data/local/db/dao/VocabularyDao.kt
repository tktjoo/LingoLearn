package com.lingolearn.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lingolearn.app.data.local.db.entity.VocabularyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary")
    fun getAllVocabulary(): Flow<List<VocabularyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabulary(vocabulary: VocabularyEntity): Long

    @Update
    suspend fun updateVocabulary(vocabulary: VocabularyEntity)

    @Delete
    suspend fun deleteVocabulary(vocabulary: VocabularyEntity)
}
