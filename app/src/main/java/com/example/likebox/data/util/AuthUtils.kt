package com.example.likebox.data.util

import java.util.regex.Pattern

object PhoneNumberValidator {
    // 010으로 시작하는 패턴만 허용
    private val KOREA_PHONE_PATTERN = "^(010)([0-9]{8})\$".toRegex()

    fun isValidPhoneNumber(number: String): Boolean {
        val digitsOnly = number.filter { it.isDigit() }

        // 11자리가 아닌 경우 false
        if (digitsOnly.length != 11) {
            return false
        }

        // 010으로 시작하지 않는 경우 false
        if (!digitsOnly.startsWith("010")) {
            return false
        }

        return KOREA_PHONE_PATTERN.matches(digitsOnly)
    }

    fun formatPhoneNumber(number: String, countryCode: String = "+82"): String {
        val digitsOnly = number.filter { it.isDigit() }

        if (!isValidPhoneNumber(digitsOnly)) {
            throw IllegalArgumentException("Invalid phone number format")
        }

        return "${countryCode}10${digitsOnly.substring(3)}"
    }
}

object SpotifyConfig {
    const val CLIENT_ID = "8feb61fef99a40b980c062841e6801e4"
    const val REDIRECT_URI = "com.example.likebox://callback"
    const val SCOPES = "user-library-read user-library-modify"
}