package com.example.likebox.domain.model.library

data class Track(
    override val id: String,
    override val platformId: String,
    override val platform: MusicPlatform,
    override val name: String,
    override val thumbnailUrl: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    val artists: List<String>,
    val album: String,
    val durationMs: Int,
) : MusicContent