package com.linguaflow.app.ui.screens.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.local.datastore.UserPreferences
import com.linguaflow.app.data.local.db.entity.VocabularyEntity
import com.linguaflow.app.domain.usecase.vocabulary.AddVocabularyUseCase
import com.linguaflow.app.domain.usecase.vocabulary.GetVocabularyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VocabularyViewModel @Inject constructor(
    getVocabularyUseCase: GetVocabularyUseCase,
    private val addVocabularyUseCase: AddVocabularyUseCase,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val targetLanguage: StateFlow<String> = userPreferences.targetLanguageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    // Only show words that match the currently selected language
    val vocabularyList: StateFlow<List<VocabularyEntity>> = userPreferences.targetLanguageFlow
        .flatMapLatest { language ->
            getVocabularyUseCase().map { words ->
                words.filter { it.language == language }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addVocabulary(
        word: String,
        translation: String,
        language: String,
        category: String
    ) {
        viewModelScope.launch {
            val newVocab = VocabularyEntity(
                word = word,
                translation = translation,
                language = language,
                category = category,
                phonetic = null,
                exampleSentence = null,
                difficultyLevel = 1,
                nextReviewDate = System.currentTimeMillis() + 86400000, // 1 day from now
                createdAt = System.currentTimeMillis()
            )
            addVocabularyUseCase(newVocab)
        }
    }
}
