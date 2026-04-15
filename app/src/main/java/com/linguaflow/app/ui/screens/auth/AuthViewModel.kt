package com.linguaflow.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.local.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val userPreferences: UserPreferences
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun authenticateWithEmail(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _error.value = "Preencha todos os campos."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            // Simular envio de OTP para o email
            kotlinx.coroutines.delay(1000)
            _otpSent.value = true
            _isLoading.value = false
            _error.value = null
        }
    }

    fun authenticateWithGoogle() {
        viewModelScope.launch {
            _isLoading.value = true
            // Aqui será integrada a API do Google futuramente.
            // Para já, assumimos sucesso imediato do Google SignIn.
            kotlinx.coroutines.delay(1000)
            userPreferences.setLoggedIn(true)
            _isLoading.value = false
        }
    }

    fun verifyOtp(code: String) {
        if (code.length < 4) {
            _error.value = "Código inválido."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            // Simular verificação na API
            kotlinx.coroutines.delay(1000)
            userPreferences.setLoggedIn(true)
            _isLoading.value = false
        }
    }

    fun completeOnboarding(languageCode: String) {
        viewModelScope.launch {
            userPreferences.setTargetLanguage(languageCode)
            userPreferences.setHasCompletedOnboarding(true)
        }
    }

    fun clearError() {
        _error.value = null
    }
}
