package com.example.likebox.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val authState = authRepository.authState
    val currentUser = authRepository.currentUser

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signIn(email, password)
                .onSuccess {
                    _uiEvent.emit(AuthUiEvent.SignInSuccess)
                }
                .onFailure { e ->
                    _uiEvent.emit(AuthUiEvent.ShowError(e.message ?: "Sign in failed"))
                }
        }
    }

    fun signUp(email: String, password: String, nickname: String) {
        viewModelScope.launch {
            authRepository.signUp(email, password, nickname)
                .onSuccess {
                    _uiEvent.emit(AuthUiEvent.NavigateToPlatformSetup)
                }
                .onFailure { e ->
                    _uiEvent.emit(AuthUiEvent.ShowError(e.message ?: "Sign up failed"))
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
                .onSuccess {
                    _uiEvent.emit(AuthUiEvent.SignOutSuccess)
                }
                .onFailure { e ->
                    _uiEvent.emit(AuthUiEvent.ShowError(e.message ?: "Sign out failed"))
                }
        }
    }
}

sealed class AuthUiEvent {
    data object SignInSuccess : AuthUiEvent()
    data object SignOutSuccess : AuthUiEvent()
    data object NavigateToPlatformSetup : AuthUiEvent()
    data class ShowError(val message: String) : AuthUiEvent()
}
