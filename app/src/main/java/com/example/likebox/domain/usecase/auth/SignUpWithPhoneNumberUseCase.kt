package com.example.likebox.domain.usecase.auth

import android.app.Activity
import com.example.likebox.data.util.PhoneNumberValidator
import com.example.likebox.di.Mock
import com.example.likebox.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpWithPhoneNumberUseCase @Inject constructor(
    @Mock private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        activity: Activity,
        password: String
    ): Result<Unit> {
        if (!PhoneNumberValidator.isValidPhoneNumber(phoneNumber)) {
            return Result.failure(IllegalArgumentException("Please enter a valid phone number"))
        }

        if (!isValidPassword(password)) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        val formattedNumber = PhoneNumberValidator.formatPhoneNumber(phoneNumber)
        return authRepository.signUpWithPhoneNumber(formattedNumber, activity, password)
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}