package com.example.likebox.domain.model.library

data class Album(
    override val id: String,
    override val platformId: String,
    override val platform: MusicPlatform,
    override val name: String,
    override val thumbnailUrl: String,
    override val updatedAt: Long,
    override val createdAt: Long,
    val artists: List<String>,
    val tracks: List<Track>,
    val releaseDate: Long,
    val trackCount: Int,
    val tracks: List<String> // Feat: track.id(String) list
) : MusicContent