package com.example.likebox.domain.usecase.auth

import com.example.likebox.domain.repository.AuthRepository
import com.example.likebox.domain.repository.PlatformRepository
import javax.inject.Inject

class CheckAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val platformRepository: PlatformRepository
) {
    suspend operator fun invoke(): AuthState {
        return when {
//            !authRepository.isLoggedIn() -> AuthState.NotAuthenticated
//            !platformRepository.hasAnyPlatformConnected() -> AuthState.NeedsPlatformSetup
            else -> AuthState.Authenticated
        }
    }
}

sealed interface AuthState {
    object Authenticated : AuthState
    object NotAuthenticated : AuthState
    object NeedsPlatformSetup : AuthState
}