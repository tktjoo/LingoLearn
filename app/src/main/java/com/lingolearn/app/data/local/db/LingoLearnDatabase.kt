package com.lingolearn.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lingolearn.app.data.local.db.dao.UserDao
import com.lingolearn.app.data.local.db.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 3,
    exportSchema = false
)
abstract class LingoLearnDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
