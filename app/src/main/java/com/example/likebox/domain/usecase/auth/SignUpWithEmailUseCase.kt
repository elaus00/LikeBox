package com.example.likebox.domain.usecase.auth

import com.example.likebox.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, nickname: String): Result<Unit> {
        if (!isValidEmail(email)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        if (!isValidPassword(password)) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }
        if (!isValidNickname(nickname)) {
            return Result.failure(IllegalArgumentException("Nickname must be between 2 and 20 characters"))
        }

        return authRepository.signUp(email, password, nickname)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun isValidNickname(nickname: String): Boolean {
        return nickname.length in 2..20
    }
}