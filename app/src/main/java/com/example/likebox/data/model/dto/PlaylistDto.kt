package com.example.likebox.data.model.dto

import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.model.Playlist
import com.google.gson.annotations.SerializedName

data class PlaylistDto(
    override val id: String,
    override val pid: String,
    override val platform: String,
    override val name: String,
    override val thumbnailUrl: String,
    val description: String?,
    val tracks: List<String>,
    val owner: String,
    val trackCount: Int
) : MusicContentDto<Playlist> {

    override fun toDomain(createdAt: Long, updatedAt: Long): Playlist {
        return Playlist(
            id = id,
            platformId = pid,
            platform = MusicPlatform.fromId(platform),
            name = name,
            thumbnailUrl = thumbnailUrl,
            updatedAt = updatedAt,
            createdAt = createdAt,
            description = description,
            trackCount = trackCount,
            owner = owner,
            tracks = tracks
        )
    }

    companion object {
        fun fromDomain(domain: Playlist): PlaylistDto {
            return PlaylistDto(
                id = domain.id,
                pid = domain.platformId,
                platform = domain.platform.name,
                name = domain.name,
                thumbnailUrl = domain.thumbnailUrl,
                description = domain.description,
                tracks = domain.tracks,
                owner = domain.owner,
                trackCount = domain.trackCount
            )
        }
    }
}