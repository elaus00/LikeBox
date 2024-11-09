package com.example.likebox.data.repository

import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.domain.model.Track
import com.example.likebox.domain.model.Playlist
import com.example.likebox.domain.model.Album
import com.example.likebox.domain.model.ContentType
import com.example.likebox.domain.model.MusicContent
import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.repository.MusicRepository
import javax.inject.Inject


class MusicRepositoryImpl @Inject constructor(
    private val firebaseService: FirebaseService
) : MusicRepository {
    override suspend fun getLikedTracks(platform: MusicPlatform): Result<List<Track>> {
        TODO()
    }

    override suspend fun getLikedContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<List<MusicContent>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToLiked(content: MusicContent): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromLiked(content: MusicContent): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun syncPlatformContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}