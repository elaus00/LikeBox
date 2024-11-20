package com.example.likebox.presentation.state.auth

data class SignUpState(
    val signUpMethod: SignUpMethod = SignUpMethod.EMAIL,
    val emailOrPhone: String = "",
    val isVerified: Boolean = false,
    val password: String = "",
    val username: String = ""
)

enum class SignUpMethod {
    EMAIL, PHONE
}