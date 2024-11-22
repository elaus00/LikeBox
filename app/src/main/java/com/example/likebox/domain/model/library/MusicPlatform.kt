package com.example.likebox.domain.model.library

enum class MusicPlatform {
    SPOTIFY,
    APPLE_MUSIC,
    YOUTUBE_MUSIC,
    MELON,
    GENIE,
    FLOO,
    TIDAL,
    AMAZON_MUSIC;

    companion object {
        fun fromId(id: String): MusicPlatform = valueOf(id.uppercase())
    }
}