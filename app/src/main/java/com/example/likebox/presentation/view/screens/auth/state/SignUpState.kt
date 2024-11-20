package com.example.likebox.presentation.view.screens.auth.state

data class SignUpState(
    val signUpMethod: SignUpMethod = SignUpMethod.EMAIL,
    val emailOrPhone: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val username: String = "",
    val isVerificationSent: Boolean = false,
    val showPassword: Boolean = false,
    val showPasswordConfirmation: Boolean = false,
    val isLoading: Boolean = false,
    val emailOrPhoneError: String? = null,
    val passwordError: String? = null,
    val usernameError: String? = null,
    val passwordConfirmationError: String? = null
)

enum class SignUpMethod {
    EMAIL, PHONE
}