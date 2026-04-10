package com.linguaflow.app.di

import com.linguaflow.app.data.repository.StreakRepositoryImpl
import com.linguaflow.app.data.repository.VocabularyRepositoryImpl
import com.linguaflow.app.domain.repository.StreakRepository
import com.linguaflow.app.domain.repository.VocabularyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVocabularyRepository(
        vocabularyRepositoryImpl: VocabularyRepositoryImpl
    ): VocabularyRepository

    @Binds
    @Singleton
    abstract fun bindStreakRepository(
        streakRepositoryImpl: StreakRepositoryImpl
    ): StreakRepository

    @Binds
    @Singleton
    abstract fun bindSpeechRepository(
        speechRepositoryImpl: com.linguaflow.app.data.repository.SpeechRepositoryImpl
    ): com.linguaflow.app.domain.repository.SpeechRepository

    @Binds
    @Singleton
    abstract fun bindConversationRepository(
        conversationRepositoryImpl: com.linguaflow.app.data.repository.ConversationRepositoryImpl
    ): com.linguaflow.app.domain.repository.ConversationRepository
}
