package com.example.likebox.data.repository.mock

import com.example.likebox.domain.model.auth.User
import com.example.likebox.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockUserRepositoryImpl @Inject constructor() : UserRepository {
    private var mockUser = User(
        userId = "mock_user_id",
        email = "test@example.com",
        phoneNumber = 1234567890,
        nickName = "Test User",
        profilePictureUrl = null,
        connectedPlatforms = listOf("spotify", "apple_music")
    )

    override suspend fun getCurrentUser(): Flow<User> = flow {
        delay(500) // 네트워크 지연 시뮬레이션
        emit(mockUser)
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        delay(500)
        mockUser = user
        return Result.success(Unit)
    }

    override suspend fun updateProfileImage(imageUri: String): Result<String> {
        delay(1000)
        val newImageUrl = "https://example.com/mock_image_${System.currentTimeMillis()}"
        mockUser = mockUser.copy(profilePictureUrl = newImageUrl)
        return Result.success(newImageUrl)
    }

    override suspend fun deleteUser(): Result<Unit> {
        delay(500)
        mockUser = User(
            userId = "mock_user_id",
            email = "",
            phoneNumber = 0,
            nickName = "",
            profilePictureUrl = null,
            connectedPlatforms = emptyList()
        )
        return Result.success(Unit)
    }

    override suspend fun signOut(): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }
}