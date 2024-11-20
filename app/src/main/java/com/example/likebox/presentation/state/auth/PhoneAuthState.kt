package com.example.likebox.presentation.state.auth

data class PhoneAuthState(
    val phoneNumber: String = "",
    val verificationCode: String = "",
    val phoneNumberError: String? = null,
    val verificationCodeError: String? = null,
    val verificationId: String? = null,  // Firebase에서 받은 verificationId 저장
    val isPhoneNumberVerified: Boolean = false,
    val isLoading: Boolean = false,
    val authStep: PhoneAuthStep = PhoneAuthStep.PHONE_NUMBER
)

enum class PhoneAuthStep {
    PHONE_NUMBER,    // 전화번호 입력 단계
    VERIFICATION,    // 인증번호 입력 단계
    COMPLETED       // 인증 완료 단계
}