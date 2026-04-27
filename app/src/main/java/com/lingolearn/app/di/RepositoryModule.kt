package com.lingolearn.app.di

import com.lingolearn.app.data.repository.StreakRepositoryImpl
import com.lingolearn.app.data.repository.VocabularyRepositoryImpl
import com.lingolearn.app.domain.repository.StreakRepository
import com.lingolearn.app.domain.repository.VocabularyRepository
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
        speechRepositoryImpl: com.lingolearn.app.data.repository.SpeechRepositoryImpl
    ): com.lingolearn.app.domain.repository.SpeechRepository

    @Binds
    @Singleton
    abstract fun bindConversationRepository(
        conversationRepositoryImpl: com.lingolearn.app.data.repository.ConversationRepositoryImpl
    ): com.lingolearn.app.domain.repository.ConversationRepository
}
