package com.example.likebox.presentation.view.screens.auth.state

data class SignInState(
    val signInMethod: SignInMethod = SignInMethod.EMAIL,
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val phoneNumberError: String? = null,
    val passwordError: String? = null
)


enum class SignInMethod {
    EMAIL, PHONE
}