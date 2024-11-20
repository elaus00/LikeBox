package com.example.likebox.domain.repository

import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.PlatformAuth

interface PlatformAuthRepository {
    suspend fun getAuthInfo(platform: MusicPlatform): Result<PlatformAuth>
    suspend fun updateAuthInfo(auth: PlatformAuth): Result<Unit>
    suspend fun clearAuthInfo(platform: MusicPlatform): Result<Unit>
    suspend fun hasAnyPlatformConnected(platform: MusicPlatform): Result<Unit>
}