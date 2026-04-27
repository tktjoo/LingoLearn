package com.lingolearn.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lingolearn.app.data.local.db.dao.StreakDao
import com.lingolearn.app.data.local.db.dao.UserDao
import com.lingolearn.app.data.local.db.dao.VocabularyDao
import com.lingolearn.app.data.local.db.entity.StreakEntity
import com.lingolearn.app.data.local.db.entity.UserEntity
import com.lingolearn.app.data.local.db.entity.VocabularyEntity

@Database(
    entities = [VocabularyEntity::class, StreakEntity::class, UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class LingoLearnDatabase : RoomDatabase() {
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun streakDao(): StreakDao
    abstract fun userDao(): UserDao
}
