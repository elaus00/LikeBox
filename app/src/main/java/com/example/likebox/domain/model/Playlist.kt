package com.example.likebox.domain.model

import java.time.LocalDateTime

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
    val tracks: List<String> // Fix: track 인스턴스 list -> track.id(String) list
) : MusicContent