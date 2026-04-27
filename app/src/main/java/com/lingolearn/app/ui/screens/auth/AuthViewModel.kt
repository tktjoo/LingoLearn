package com.lingolearn.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingolearn.app.BuildConfig
import com.lingolearn.app.data.local.datastore.UserPreferences
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.lingolearn.app.data.remote.emailjs.EmailJsApi
import com.lingolearn.app.data.remote.emailjs.EmailJsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import android.util.Log
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val emailJsApi: EmailJsApi,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    val hasCompletedOnboardingFlow = userPreferences.hasCompletedOnboardingFlow
    val isLoggedInFlow = userPreferences.isLoggedInFlow

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent.asStateFlow()

    private val _generatedOtp = MutableStateFlow<String?>(null)

    private val _targetEmail = MutableStateFlow<String?>(null)
    val targetEmail: StateFlow<String?> = _targetEmail.asStateFlow()

    private var pendingRegistrationName: String? = null

    fun startLoginProcess(email: String) {
        if (email.isBlank()) {
            _error.value = "Introduza o seu email."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val document = firestore.collection("users").document(email).get().await()
                if (!document.exists()) {
                    _isLoading.value = false
                    _error.value = "Conta não encontrada. Por favor, faça o registo."
                    return@launch
                }
                val userName = document.getString("name") ?: "Utilizador"
                pendingRegistrationName = null // We are logging in
                sendOtpEmail(email, userName)
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Erro ao verificar a conta. Verifique a sua internet."
                Log.e("AuthViewModel", "Firestore Get Error", e)
            }
        }
    }

    fun startRegisterProcess(email: String, name: String) {
        if (email.isBlank() || name.isBlank()) {
            _error.value = "Preencha todos os campos."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val document = firestore.collection("users").document(email).get().await()
                if (document.exists()) {
                    _isLoading.value = false
                    _error.value = "Este e-mail já está registado. Faça login."
                    return@launch
                }
                pendingRegistrationName = name
                sendOtpEmail(email, name)
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Erro ao verificar a conta. Verifique a sua internet."
                Log.e("AuthViewModel", "Firestore Get Error", e)
            }
        }
    }

    private suspend fun sendOtpEmail(email: String, name: String) {
        try {
            val otp = (100000..999999).random().toString()
            _generatedOtp.value = otp
            _targetEmail.value = email

            val params = mapOf("to_email" to email, "to_name" to name, "passcode" to otp)
            val request = EmailJsRequest(
                service_id = "service_w949h2h",
                template_id = "template_vro0nya",
                user_id = "77Jjy0wC9moXFpYAl",
                accessToken = "Ue8TcLZDNpIg3udMiep51",
                template_params = params
            )
            emailJsApi.sendEmail(request)
            _otpSent.value = true
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthViewModel", "EmailJS Error (HTTP ${e.code()}): $errorBody")
            _error.value = "Erro ao enviar email OTP: ${e.code()} - $errorBody"
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error sending OTP email", e)
            _error.value = "Erro ao enviar email OTP: ${e.localizedMessage}"
        } finally {
            _isLoading.value = false
        }
    }

    fun verifyOtp(code: String) {
        if (code == _generatedOtp.value) {
            viewModelScope.launch {
                val currentEmail = _targetEmail.value ?: return@launch
                var finalName = ""

                // If it's a registration, save the user
                if (pendingRegistrationName != null) {
                    try {
                        val userMap = hashMapOf("name" to pendingRegistrationName!!, "email" to currentEmail)
                        firestore.collection("users").document(currentEmail).set(userMap).await()
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Firestore Set Error", e)
                    }
                    finalName = pendingRegistrationName!!
                    pendingRegistrationName = null
                } else {
                    // It's a login, fetch the existing name to save in DataStore
                    try {
                        val document = firestore.collection("users").document(currentEmail).get().await()
                        finalName = document.getString("name") ?: "Utilizador"
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Firestore Get Error", e)
                        finalName = "Utilizador"
                    }
                }

                userPreferences.setUserProfile(name = finalName, email = currentEmail)
                userPreferences.setLoggedIn(true)
                _otpSent.value = false
            }
        } else {
            _error.value = "Código inválido."
        }
    }

    fun resetOtpState() {
        _otpSent.value = false
        _error.value = null
    }

    fun completeOnboarding(languageCode: String) {
        viewModelScope.launch {
            userPreferences.setTargetLanguage(languageCode)
            userPreferences.setHasCompletedOnboarding(true)
        }
    }

    fun clearError() { _error.value = null }
}