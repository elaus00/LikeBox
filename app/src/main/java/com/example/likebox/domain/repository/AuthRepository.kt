package com.example.likebox.domain.repository

import android.app.Activity
import com.example.likebox.domain.model.auth.User
import com.example.likebox.presentation.state.auth.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState>
    val currentUser: StateFlow<User?>

    // 기존 인증 메서드들
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String, nickname: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun getCurrentUser(): Result<User>

    // 전화번호 인증 관련 메서드들
    suspend fun signInWithPhoneNumber(phoneNumber: String, activity: Activity, password: String): Result<Unit>
    suspend fun verifyPhoneCode(verificationCode: String): Result<Unit>
    suspend fun resendVerificationCode(phoneNumber: String, activity: Activity): Result<Unit>

    // 이메일 인증 관련 메서드
    suspend fun checkEmailVerification(): Result<Boolean>

    fun resetPhoneAuth()
}