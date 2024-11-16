package com.example.likebox.domain.model.auth

data class User(
    val userId: String,
    val email: String,
    val phoneNumber: Number,
    val nickName: String,
    val profilePictureUrl: String?,
    val connectedPlatforms: List<String> // 연결된 플랫폼 ID 리스트
)