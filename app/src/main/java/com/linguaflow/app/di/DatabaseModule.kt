package com.linguaflow.app.di

import android.content.Context
import androidx.room.Room
import com.linguaflow.app.data.local.db.LingoLearnDatabase
import com.linguaflow.app.data.local.db.dao.StreakDao
import com.linguaflow.app.data.local.db.dao.UserDao
import com.linguaflow.app.data.local.db.dao.VocabularyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLingoLearnDatabase(
        @ApplicationContext context: Context
    ): LingoLearnDatabase {
        return Room.databaseBuilder(
            context,
            LingoLearnDatabase::class.java,
            "lingolearn_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideVocabularyDao(database: LingoLearnDatabase): VocabularyDao {
        return database.vocabularyDao()
    }

    @Provides
    fun provideStreakDao(database: LingoLearnDatabase): StreakDao {
        return database.streakDao()
    }

    @Provides
    fun provideUserDao(database: LingoLearnDatabase): UserDao {
        return database.userDao()
    }
}
