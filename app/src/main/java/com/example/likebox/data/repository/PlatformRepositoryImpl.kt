package com.example.likebox.data.repository

import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.repository.PlatformRepository
import javax.inject.Inject

class PlatformRepositoryImpl @Inject constructor(
    private val firebaseService: FirebaseService  // 또는 필요한 다른 의존성
) : PlatformRepository {
    override suspend fun getConnectedPlatforms(): Result<List<MusicPlatform>> {
        return try {
            val platforms = firebaseService.getConnectedPlatforms()
            Result.success(platforms)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}