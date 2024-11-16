package com.example.likebox.domain.model

enum class MusicPlatform {
    SPOTIFY,
    APPLE_MUSIC;

    companion object {
        fun fromId(id: String): MusicPlatform = valueOf(id.uppercase())
    }
}