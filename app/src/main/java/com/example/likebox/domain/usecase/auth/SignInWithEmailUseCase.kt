package com.example.likebox.domain.usecase.auth

import com.example.likebox.di.Mock
import com.example.likebox.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    @Mock private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (!isValidEmail(email)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        if (!isValidPassword(password)) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        return authRepository.signInWithEmail(email, password)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}