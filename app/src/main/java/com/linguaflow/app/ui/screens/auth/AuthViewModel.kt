package com.linguaflow.app.ui.screens.auth

import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.linguaflow.app.data.local.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val userPreferences: UserPreferences
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isGoogleLoading = MutableStateFlow(false)
    val isGoogleLoading: StateFlow<Boolean> = _isGoogleLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Auto-login if Firebase has a user session
        if (auth.currentUser != null) {
            viewModelScope.launch {
                userPreferences.setLoggedIn(true)
            }
        }
    }

    fun authenticateWithEmail(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _error.value = "Preencha todos os campos."
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Try to sign in first
                auth.signInWithEmailAndPassword(email, pass).await()
                userPreferences.setLoggedIn(true)
            } catch (e: Exception) {
                // Distinguish between invalid password and user not found
                if (e is FirebaseAuthInvalidCredentialsException && e.errorCode == "ERROR_WRONG_PASSWORD") {
                    _error.value = "Password inválida."
                } else if (e is FirebaseAuthInvalidUserException ||
                           (e is FirebaseAuthInvalidCredentialsException && e.errorCode != "ERROR_WRONG_PASSWORD")) {
                    // If user not found, attempt to register
                    try {
                        auth.createUserWithEmailAndPassword(email, pass).await()
                        userPreferences.setLoggedIn(true)
                    } catch (e2: Exception) {
                        _error.value = "Erro no registo: ${e2.localizedMessage}"
                    }
                } else {
                    _error.value = "Erro na autenticação: ${e.localizedMessage}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun authenticateWithGoogle(response: GetCredentialResponse) {
        viewModelScope.launch {
            _isGoogleLoading.value = true
            _error.value = null
            try {
                val credential = response.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken

                    // Use the token to authenticate with Firebase
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential).await()

                    userPreferences.setLoggedIn(true)
                } else {
                    _error.value = "Credencial não suportada."
                }
            } catch (e: Exception) {
                _error.value = "Erro no Google Sign-In: ${e.localizedMessage}"
            } finally {
                _isGoogleLoading.value = false
            }
        }
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun setGoogleLoading(loading: Boolean) {
        _isGoogleLoading.value = loading
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
