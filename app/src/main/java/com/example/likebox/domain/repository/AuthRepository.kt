package com.example.likebox.domain.repository

import android.app.Activity
import com.example.likebox.domain.model.auth.User
import com.example.likebox.presentation.view.screens.auth.state.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    // State flows
    val authState: StateFlow<AuthState>
    val currentUser: StateFlow<User?>

    // Authentication methods
    /**
     * 이메일로 로그인
     * @param email 사용자 이메일
     * @param password 비밀번호
     * @return Result<Unit> 로그인 성공 여부
     */
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>

    /**
     * 이메일로 회원가입
     * @param email 사용자 이메일
     * @param password 비밀번호
     * @param nickname 사용자 닉네임
     * @return Result<Unit> 회원가입 성공 여부
     */
    suspend fun signUpWithEmail(email: String, password: String, nickname: String): Result<Unit>

    /**
     * 전화번호로 로그인
     * @param phoneNumber 전화번호 (국가 코드 포함)
     * @param activity 현재 액티비티 (Firebase 인증에 필요)
     * @param password 비밀번호
     * @return Result<Unit> 로그인 성공 여부
     */
    suspend fun signInWithPhoneNumber(phoneNumber: String, activity: Activity, password: String): Result<Unit>

    /**
     * 전화번호로 회원가입
     * @param phoneNumber 전화번호 (국가 코드 포함)
     * @param activity 현재 액티비티 (Firebase 인증에 필요)
     * @param password 비밀번호
     * @return Result<Unit> 회원가입 성공 여부
     */
    suspend fun signUpWithPhoneNumber(phoneNumber: String, activity: Activity, password: String): Result<Unit>

    // Verification methods
    /**
     * 전화번호 인증 코드 확인
     * @param verificationCode 사용자가 입력한 인증 코드
     * @return Result<Unit> 인증 성공 여부
     */
    suspend fun verifyPhoneCode(verificationCode: String): Result<Unit>

    /**
     * 이메일 인증 상태 확인
     * @return Result<Boolean> 이메일 인증 여부
     */
    suspend fun checkEmailVerification(): Result<Boolean>

    /**
     * 전화번호 인증 코드 재전송
     * @param phoneNumber 전화번호
     * @param activity 현재 액티비티
     * @return Result<Unit> 재전송 성공 여부
     */
    suspend fun resendVerificationCode(phoneNumber: String, activity: Activity): Result<Unit>

    // User management methods
    /**
     * 현재 유저 정보 조회
     * @return Result<User> 현재 로그인된 사용자 정보
     */
    suspend fun getCurrentUser(): Result<User>

    /**
     * 로그아웃
     * @return Result<Unit> 로그아웃 성공 여부
     */
    suspend fun signOut(): Result<Unit>

    /**
     * 회원탈퇴
     * @return Result<Unit> 회원탈퇴 성공 여부
     */
    suspend fun deleteAccount(): Result<Unit>

    // Validation methods
    /**
     * 이메일 중복 확인
     * @param email 확인할 이메일
     * @return Result<Boolean> 중복 여부 (true: 중복)
     */
    suspend fun checkEmailExists(email: String): Result<Boolean>

    // State management methods
    /**
     * 전화번호 인증 관련 상태 초기화
     */
    fun resetPhoneAuth()
}