package com.example.likebox.domain.usecase.auth

import android.app.Activity
import com.example.likebox.data.util.PhoneNumberValidator
import com.example.likebox.di.Mock
import com.example.likebox.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithPhoneNumberUseCase @Inject constructor(
    @Mock private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, activity: Activity, password: String): Result<Unit> {
        if (!PhoneNumberValidator.isValidPhoneNumber(phoneNumber)) {
            return Result.failure(IllegalArgumentException("Invalid phone number format"))
        }

        val formattedNumber = PhoneNumberValidator.formatPhoneNumber(phoneNumber)
        return authRepository.signInWithPhoneNumber(formattedNumber, activity, password)
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}