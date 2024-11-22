package com.example.likebox.data.repository.mock

import android.app.Activity
import com.example.likebox.domain.model.auth.User
import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.presentation.view.screens.auth.state.AuthState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class MockAuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuthenticated)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // 테스트를 위한 Mock 데이터 저장소
    private val mockUsers = ConcurrentHashMap<String, User>()
    private var verificationId: String? = null

    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> = try {
        delay(500) // 네트워크 지연 시뮬레이션
        mockUsers[email]?.let { user ->
            _currentUser.value = user
            _authState.value = AuthState.NeedsPlatformSetup
            Result.success(Unit)
        } ?: Result.failure(Exception("User not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signInWithPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        password: String
    ): Result<Unit> = try {
        delay(500)
        verificationId = "mock-verification-id"
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        nickname: String
    ): Result<Unit> = try {
        delay(500)
        if (mockUsers.containsKey(email)) {
            Result.failure(Exception("Email already exists"))
        } else {
            val newUser = User(
                userId = "mock-${mockUsers.size + 1}",
                email = email,
                nickName = nickname,
                phoneNumber = null,
                profilePictureUrl = null,
                connectedPlatforms = emptyList()
            )
            mockUsers[email] = newUser
            _currentUser.value = newUser
            _authState.value = AuthState.NeedsPlatformSetup
            Result.success(Unit)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUpWithPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        password: String
    ): Result<Unit> = try {
        delay(500)
        verificationId = "mock-verification-id"
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun verifyPhoneCode(verificationCode: String): Result<Unit> = try {
        delay(300)
        val newUser = User(
            userId = "mock-${mockUsers.size + 1}",
            email = "",
            nickName = "",
            phoneNumber = 1234567890,
            profilePictureUrl = null,
            connectedPlatforms = emptyList()
        )
        _currentUser.value = newUser
        _authState.value = AuthState.NeedsPlatformSetup
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun checkEmailVerification(): Result<Boolean> = try {
        delay(300)
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Result<Unit> = try {
        delay(300)
        verificationId = "mock-verification-id"
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCurrentUser(): Result<User> = try {
        delay(300)
        _currentUser.value?.let {
            Result.success(it)
        } ?: Result.failure(Exception("No authenticated user"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signOut(): Result<Unit> = try {
        delay(300)
        _currentUser.value = null
        _authState.value = AuthState.NotAuthenticated
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteAccount(): Result<Unit> = try {
        delay(500)
        _currentUser.value?.let { user ->
            mockUsers.remove(user.email)
        }
        _currentUser.value = null
        _authState.value = AuthState.NotAuthenticated
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun checkEmailExists(email: String): Result<Boolean> = try {
        delay(300)
        Result.success(mockUsers.containsKey(email))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun resetPhoneAuth() {
        verificationId = null
    }

    // 테스트 헬퍼 메서드들
    fun reset() {
        mockUsers.clear()
        _currentUser.value = null
        _authState.value = AuthState.NotAuthenticated
        verificationId = null
    }

    fun addMockUser(user: User) {
        mockUsers[user.email] = user
    }

    fun setMockAuthState(state: AuthState) {
        _authState.value = state
    }
}