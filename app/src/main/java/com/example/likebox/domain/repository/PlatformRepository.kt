package com.example.likebox.domain.repository

import com.example.likebox.domain.model.MusicPlatform

interface PlatformRepository {
    suspend fun getConnectedPlatforms(): Result<List<MusicPlatform>>
}
