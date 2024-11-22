package com.example.likebox.presentation.view.screens.auth.viewmodel

import android.app.Activity
import android.telephony.PhoneNumberUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.likebox.di.Mock
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.domain.usecase.auth.SignInWithEmailUseCase
import com.example.likebox.domain.usecase.auth.SignInWithPhoneNumberUseCase
import com.example.likebox.domain.usecase.auth.SignUpWithEmailUseCase
import com.example.likebox.domain.usecase.auth.SignUpWithPhoneNumberUseCase
import com.example.likebox.presentation.view.screens.auth.state.PhoneAuthState
import com.example.likebox.presentation.view.screens.auth.state.SignInMethod
import com.example.likebox.presentation.view.screens.auth.state.SignInState
import com.example.likebox.presentation.view.screens.auth.state.SignUpMethod
import com.example.likebox.presentation.view.screens.auth.state.SignUpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithPhoneNumberUseCase: SignInWithPhoneNumberUseCase,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val signUpWithPhoneNumberUseCase: SignUpWithPhoneNumberUseCase,
    @Mock private val authRepository: AuthRepository
) : ViewModel() {

    val authState = authRepository.authState
    val currentUser = authRepository.currentUser

    private val _uiEvent =
        MutableSharedFlow<AuthUiEvent>(replay = 0) // No replay ensures fresh emission
    val uiEvent = _uiEvent.asSharedFlow()

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _phoneAuthState = MutableStateFlow(PhoneAuthState())
    val phoneAuthState = _phoneAuthState.asStateFlow()

    private lateinit var _activity: Activity

    fun setActivity(activity: Activity) {
        _activity = activity
    }

    fun updateSignInMethod(method: SignInMethod) {
        _signInState.update {
            SignInState(
                signInMethod = method
            )  // Reset all fields when changing method
        }
    }

    fun updateSignUpMethod(method: SignUpMethod) {
        _signUpState.update { currentState ->
            currentState.copy(
                signUpMethod = method,
                emailOrPhone = "",  // 입력 필드 초기화
                isVerificationSent = false  // 인증 상태 초기화
            )
        }
    }

    fun updateEmail(email: String) {
        _signInState.update { currentState ->
            currentState.copy(
                email = email,
                emailError = if (!isValidEmail(email) && email.isNotEmpty())
                    "Invalid email format" else null
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _signInState.update { currentState ->
            currentState.copy(
                phoneNumber = phoneNumber,
                phoneNumberError = if (!isValidPhoneNumber(phoneNumber) && phoneNumber.isNotEmpty())
                    "Invalid phone number format" else null
            )
        }
    }

    fun updatePassword(password: String) {
        _signInState.update { currentState ->
            currentState.copy(
                password = password,
                passwordError = if (!isValidPassword(password) && password.isNotEmpty())
                    "Password must be at least 6 characters" else null
            )
        }
    }

    fun signIn() {
        viewModelScope.launch {
            val currentState = _signInState.value

            // Validate input based on sign in method
            when (currentState.signInMethod) {
                SignInMethod.EMAIL -> {
                    if (!isValidEmail(currentState.email)) {
                        _uiEvent.emit(AuthUiEvent.ShowError("Invalid email format"))
                        return@launch
                    }
                }

                SignInMethod.PHONE -> {
                    if (!isValidPhoneNumber(currentState.phoneNumber)) {
                        _uiEvent.emit(AuthUiEvent.ShowError("Invalid phone number format"))
                        return@launch
                    }
                }
            }

            if (!isValidPassword(currentState.password)) {
                _uiEvent.emit(AuthUiEvent.ShowError("Password must be at least 6 characters"))
                return@launch
            }

            _signInState.update { it.copy(isLoading = true) }

            try {
                val result = when (currentState.signInMethod) {
                    SignInMethod.EMAIL ->
                        signInWithEmailUseCase(currentState.email, currentState.password)

                    SignInMethod.PHONE ->
                        signInWithPhoneNumberUseCase(
                            currentState.phoneNumber,
                            _activity,
                            currentState.password
                        )
                }

                result.onSuccess {
                    _uiEvent.emit(AuthUiEvent.SignInSuccess)
                }.onFailure { e ->
                    _uiEvent.emit(AuthUiEvent.ShowError(e.message ?: "Sign in failed"))
                }
            } finally {
                _signInState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setPassword(password: String) {
        _signUpState.update { it.copy(password = password) }
    }

    fun setPasswordConfirmation(passwordConfirmation: String) {
        _signUpState.update { it.copy(passwordConfirmation = passwordConfirmation) }
    }

    fun toggleSignUpPasswordVisibility() {
        _signUpState.update { it.copy(showPassword = !it.showPassword) }
    }

    fun toggleSignUpPasswordConfirmationVisibility() {
        _signUpState.update { it.copy(showPasswordConfirmation = !it.showPasswordConfirmation) }
    }

    fun toggleSignInPasswordVisibility() {
        _signInState.update { it.copy(showPassword = !it.showPassword) }
    }

    fun signUpWithEmail(email: String, password: String, nickname: String) {
        viewModelScope.launch {
            val currentState = _signUpState.value
            if (password != currentState.passwordConfirmation) {
                _uiEvent.emit(AuthUiEvent.ShowError("Passwords do not match"))
                return@launch
            }
            _signUpState.update { it.copy(isLoading = true) }

            signUpWithEmailUseCase(email, password, nickname)
                .onSuccess {
                    _uiEvent.emit(AuthUiEvent.NavigateToPlatformSetup)
                }
                .onFailure { e ->
                    _uiEvent.emit(AuthUiEvent.ShowError(e.message ?: "Sign up failed"))
                }
        }
    }

    fun signUpWithPhoneNumber(phoneNumber: String, activity: Activity, password: String) {
        viewModelScope.launch {
            val currentState = _signUpState.value
            if (password != currentState.passwordConfirmation) {
                _uiEvent.emit(AuthUiEvent.ShowError("Passwords do not match"))
                return@launch
            }
            _signUpState.update { it.copy(isLoading = true) }

            signUpWithPhoneNumberUseCase(phoneNumber, activity, password)
                .onSuccess {
                    _uiEvent.emit(AuthUiEvent.NavigateToPlatformSetup)
                }
                .onFailure { e ->
                    _uiEvent.emit(AuthUiEvent.ShowError(e.message ?: "Sign up failed"))
                }
        }
    }

    fun logOut() {
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

    private fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPhoneNumber(phoneNumber: String): Boolean =
        PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)

    private fun isValidPassword(password: String): Boolean =
        password.length >= 6


    fun updateEmailOrPhone(value: String) {
        _signUpState.update { it.copy(emailOrPhone = value) }
    }

    fun setUsername(username: String) {
        _signUpState.update { it.copy(username = username) }
    }

    fun verifyPhoneCode(verificationCode: String) {

    }



}


sealed class AuthUiEvent {
    data object SignInSuccess : AuthUiEvent()
    data object SignOutSuccess : AuthUiEvent()
    data object NavigateToPlatformSetup : AuthUiEvent()
    data class ShowError(val message: String) : AuthUiEvent()
}