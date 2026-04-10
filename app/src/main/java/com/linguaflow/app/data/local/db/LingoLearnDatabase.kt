package com.linguaflow.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.linguaflow.app.data.local.db.dao.StreakDao
import com.linguaflow.app.data.local.db.dao.VocabularyDao
import com.linguaflow.app.data.local.db.entity.StreakEntity
import com.linguaflow.app.data.local.db.entity.VocabularyEntity

@Database(
    entities = [VocabularyEntity::class, StreakEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LingoLearnDatabase : RoomDatabase() {
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun streakDao(): StreakDao
}
