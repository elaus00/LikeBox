package com.example.likebox.data.model.dto

import com.google.firebase.Timestamp

data class MusicContentDto(
    val id: String = "",
    val platform: String = "",  // "SPOTIFY" or "APPLE_MUSIC"
    val contentType: String = "", // "TRACK", "PLAYLIST", "ALBUM"
    val name: String = "",
    val platformSpecificId: String = "",  // 각 플랫폼의 고유 ID
    val thumbnail: String = "",
    val addedAt: Timestamp = Timestamp.now(),
    // ... 공통 필드

    // Track specific fields
    val artists: List<String>? = null,
    val album: String? = null,

    // Playlist specific fields
    val description: String? = null,
    val trackCount: Int? = null,

    // Album specific fields
    val releaseDate: Timestamp? = null
)
