package com.linguaflow.app.ui.screens.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.local.db.entity.VocabularyEntity
import com.linguaflow.app.domain.usecase.vocabulary.AddVocabularyUseCase
import com.linguaflow.app.domain.usecase.vocabulary.GetVocabularyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyViewModel @Inject constructor(
    getVocabularyUseCase: GetVocabularyUseCase,
    private val addVocabularyUseCase: AddVocabularyUseCase
) : ViewModel() {

    val vocabularyList: StateFlow<List<VocabularyEntity>> = getVocabularyUseCase()
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
