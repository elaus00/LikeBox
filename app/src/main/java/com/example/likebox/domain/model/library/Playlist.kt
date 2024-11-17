package com.example.likebox.domain.model.library

data class Playlist(
    override val id: String,
    override val platformId: String,
    override val platform: MusicPlatform,
    override val name: String,
    override val thumbnailUrl: String,
    override val updatedAt: Long,
    override val createdAt: Long,
    val description: String?,
    val trackCount: Int,
    val owner: String,
    val tracks: List<Track>
) : MusicContent