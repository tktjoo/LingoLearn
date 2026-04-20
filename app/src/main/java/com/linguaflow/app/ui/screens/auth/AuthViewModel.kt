package com.linguaflow.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linguaflow.app.data.local.datastore.UserPreferences
import com.linguaflow.app.data.remote.emailjs.EmailJsApi
import com.linguaflow.app.data.remote.emailjs.EmailJsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val userPreferences: UserPreferences,
    private val emailJsApi: EmailJsApi
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent

    private val _generatedOtp = MutableStateFlow<String?>(null)

    private val _targetEmail = MutableStateFlow<String?>(null)
    val targetEmail: StateFlow<String?> = _targetEmail

    private fun sendOtpEmail(email: String, name: String) {
        // Run network call in a try/catch, keep loading state active during this time if called from login/register
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Generate a 6-digit OTP
                val otp = (100000..999999).random().toString()
                _generatedOtp.value = otp
                _targetEmail.value = email

                val params = mapOf(
                    "to_email" to email,
                    "to_name" to name,
                    "otp_code" to otp
                )
                val request = EmailJsRequest(
                    service_id = "service_w949h2h",
                    template_id = "template_vro0nya",
                    user_id = "77Jjy0wC9moXFpYAl",
                    template_params = params
                )
                emailJsApi.sendEmail(request)

                _otpSent.value = true
            } catch (e: Exception) {
                _error.value = "Erro ao enviar email OTP: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun verifyOtp(code: String) {
        if (code == _generatedOtp.value) {
            viewModelScope.launch {
                userPreferences.setLoggedIn(true)
                _otpSent.value = false // Reset state after success
            }
        } else {
            _error.value = "Código inválido."
        }
    }

    fun resetOtpState() {
        _otpSent.value = false
    }

    fun loginWithEmail(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _error.value = "Preencha todos os campos."
            return
        }
        // Without Firebase, we simulate successful verification by sending OTP immediately.
        sendOtpEmail(email, "Utilizador")
    }

    fun registerWithEmail(name: String, email: String, pass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _error.value = "Preencha todos os campos."
            return
        }
        // Without Firebase, we simulate successful verification by sending OTP immediately.
        sendOtpEmail(email, name)
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun completeOnboarding(languageCode: String) {
        viewModelScope.launch {
            userPreferences.setTargetLanguage(languageCode)
            userPreferences.setHasCompletedOnboarding(true)
        }
    }

    fun setError(msg: String) {
        _error.value = msg
    }

    fun clearError() {
        _error.value = null
    }
}
