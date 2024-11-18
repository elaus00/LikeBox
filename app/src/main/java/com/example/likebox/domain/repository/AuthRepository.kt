package com.example.likebox.domain.repository

import com.example.likebox.domain.model.auth.User
import com.example.likebox.presentation.state.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val authState: StateFlow<AuthState>
    val currentUser: StateFlow<User?>

    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String, nickname: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
}