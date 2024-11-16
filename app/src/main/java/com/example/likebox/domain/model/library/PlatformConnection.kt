package com.example.likebox.domain.model.library

data class PlatformAuth(
    val platformId: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long, // 토큰 만료 시간 (Unix 타임스탬프)
    val isValid: Boolean
)
