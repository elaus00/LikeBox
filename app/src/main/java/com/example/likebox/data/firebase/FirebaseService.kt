package com.example.likebox.data.firebase

import com.example.likebox.data.model.dto.TrackDtoFs
import com.example.likebox.domain.model.library.MusicPlatform

interface FirebaseService {
    suspend fun getLikedTracks(platform: MusicPlatform?): Result<List<TrackDtoFs>>
    suspend fun saveLikedTrack(trackDto: TrackDtoFs): Result<Unit>
    suspend fun getConnectedPlatforms(): List<MusicPlatform>
}