package com.example.likebox.data.firebase

import com.example.likebox.data.model.dto.TrackDto
import com.example.likebox.domain.model.MusicPlatform

interface FirebaseService {
    suspend fun getLikedTracks(platform: MusicPlatform): Result<List<TrackDto>>
    suspend fun saveLikedTrack(trackDto: TrackDto): Result<Unit>
    suspend fun getConnectedPlatforms(): List<MusicPlatform>
}