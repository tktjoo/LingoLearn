package com.linguaflow.app.ui.screens.streak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.local.db.entity.StreakEntity
import com.linguaflow.app.domain.usecase.streak.GetStreakUseCase
import com.linguaflow.app.domain.usecase.streak.AddXpAndCheckStreakUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StreakViewModel @Inject constructor(
    private val getStreakUseCase: GetStreakUseCase,
    private val addXpAndCheckStreakUseCase: AddXpAndCheckStreakUseCase
) : ViewModel() {

    val streak: StateFlow<StreakEntity?> = getStreakUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun testAddXP() {
        viewModelScope.launch {
            addXpAndCheckStreakUseCase(15)
        }
    }
}
