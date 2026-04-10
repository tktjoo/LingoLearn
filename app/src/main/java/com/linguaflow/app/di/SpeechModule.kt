package com.linguaflow.app.di

import android.content.Context
import com.linguaflow.app.data.remote.azure.AzureSpeechService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpeechModule {

    @Provides
    @Singleton
    fun provideAzureSpeechService(
        @ApplicationContext context: Context
    ): AzureSpeechService {
        return AzureSpeechService(context)
    }
}
