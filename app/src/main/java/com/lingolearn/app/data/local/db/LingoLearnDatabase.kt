package com.lingolearn.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lingolearn.app.data.local.db.dao.UserDao
import com.lingolearn.app.data.local.db.dao.SpeechHistoryDao
import com.lingolearn.app.data.local.db.entity.UserEntity
import com.lingolearn.app.data.local.db.entity.SpeechPracticeHistoryEntity

@Database(
    entities = [UserEntity::class, SpeechPracticeHistoryEntity::class],
    version = 4,
    exportSchema = false
)
abstract class LingoLearnDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun speechHistoryDao(): SpeechHistoryDao
}
