package com.example.likebox.domain.usecase.auth

import android.app.Activity
import com.example.likebox.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithPhoneNumberUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, activity: Activity, password : String): Result<Unit> {
        if (!isValidPhoneNumber(phoneNumber)) {
            return Result.failure(IllegalArgumentException("Invalid phone number format"))
        }

        return authRepository.signInWithPhoneNumber(phoneNumber, activity, password)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length >= 10 && phoneNumber.all { it.isDigit() }
    }

}
