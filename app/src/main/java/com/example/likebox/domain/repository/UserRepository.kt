package com.example.likebox.domain.repository

import com.example.likebox.domain.model.auth.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): Flow<User>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun updateProfileImage(imageUri: String): Result<String> // Returns new image URL
    suspend fun deleteUser(): Result<Unit>
    suspend fun signOut(): Result<Unit>
}