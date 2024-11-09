package com.example.likebox.domain.repository

import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.model.PlatformAuth

interface PlatformAuthRepository {
    suspend fun getAuthInfo(platform: MusicPlatform): Result<PlatformAuth>
    suspend fun updateAuthInfo(auth: PlatformAuth): Result<Unit>
    suspend fun clearAuthInfo(platform: MusicPlatform): Result<Unit>
}