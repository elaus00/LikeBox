package com.example.likebox.domain.repository

import com.example.likebox.domain.model.ContentType
import com.example.likebox.domain.model.MusicContent
import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.model.Track

interface MusicRepository {
    suspend fun getLikedContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<List<MusicContent>>

    suspend fun getLikedTracks(platform: MusicPlatform): Result<List<Track>>
    suspend fun addToLiked(content: MusicContent): Result<Unit>
    suspend fun removeFromLiked(content: MusicContent): Result<Unit>
    suspend fun syncPlatformContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Unit>
}