package com.example.likebox.domain.model.library

enum class MusicPlatform {
    SPOTIFY,
    APPLE_MUSIC;

    companion object {
        fun fromId(id: String): MusicPlatform = valueOf(id.uppercase())
    }
}