package com.example.likebox.domain.model.library

// Fix: 토큰 정보, expiredAt 없앰
data class PlatformAuth(
    val platform: MusicPlatform,
    val isValid: Boolean
)
