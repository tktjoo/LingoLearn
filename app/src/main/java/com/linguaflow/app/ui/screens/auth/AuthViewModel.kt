package com.linguaflow.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.linguaflow.app.data.local.datastore.UserPreferences
import com.linguaflow.app.data.remote.emailjs.EmailJsApi
import com.linguaflow.app.data.remote.emailjs.EmailJsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val userPreferences: UserPreferences,
    private val emailJsApi: EmailJsApi
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent

    private val _generatedOtp = MutableStateFlow<String?>(null)

    private val _targetEmail = MutableStateFlow<String?>(null)
    val targetEmail: StateFlow<String?> = _targetEmail

    init {
        // Sync logout state: If Firebase has no user but local preference says true, force logout.
        // We DO NOT auto-set LoggedIn = true just because Firebase has a user,
        // as the user must pass the OTP screen to officially log into the app.
        if (auth.currentUser == null) {
            viewModelScope.launch {
                userPreferences.setLoggedIn(false)
            }
        }
    }

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
                // If email fails, sign them out of Firebase to not leave them half-authenticated
                auth.signOut()
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
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = auth.signInWithEmailAndPassword(email, pass).await()
                val name = result.user?.displayName ?: "Utilizador"
                sendOtpEmail(email, name)
            } catch (e: Exception) {
                if (e is FirebaseAuthInvalidCredentialsException && e.errorCode == "ERROR_WRONG_PASSWORD") {
                    _error.value = "Password inválida."
                } else if (e is FirebaseAuthInvalidUserException ||
                           (e is FirebaseAuthInvalidCredentialsException && e.errorCode != "ERROR_WRONG_PASSWORD")) {
                    _error.value = "Conta não encontrada. Crie uma conta primeiro."
                } else {
                    _error.value = "Erro na autenticação: ${e.localizedMessage}"
                }
                _isLoading.value = false
            }
            // If successful, sendOtpEmail will handle disabling the loading state.
        }
    }

    fun registerWithEmail(name: String, email: String, pass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _error.value = "Preencha todos os campos."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = auth.createUserWithEmailAndPassword(email, pass).await()

                // Update user profile with name
                val user = result.user
                if (user != null) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user.updateProfile(profileUpdates).await()
                }

                sendOtpEmail(email, name)
            } catch (e: FirebaseAuthUserCollisionException) {
                _error.value = "Este email já está registado."
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Erro no registo: ${e.localizedMessage}"
                _isLoading.value = false
            }
            // If successful, sendOtpEmail will handle disabling the loading state.
        }
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
