package com.example.likebox.domain.model.library

interface MusicContent {
    val id: String
    val platformId: String
    val platform: MusicPlatform
    val name: String
    val thumbnailUrl: String
    val createdAt: Long
    val updatedAt : Long
}