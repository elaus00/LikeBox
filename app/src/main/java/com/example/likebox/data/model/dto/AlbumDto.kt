package com.example.likebox.data.model.dto

import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.model.Album
import com.google.gson.annotations.SerializedName

data class AlbumDto(
    override val id: String,
    override val pid: String,
    override val platform: String,
    override val name: String,
    override val thumbnailUrl: String,
    val artists: List<String>,
    val tracks: List<String>,
    val releasedDate: Long,
    val trackCount: Int
) : MusicContentDto<Album> {

    override fun toDomain(createdAt: Long, updatedAt: Long): Album {
        return Album(
            id = id,
            platformId = pid,
            platform = MusicPlatform.fromId(platform),
            name = name,
            thumbnailUrl = thumbnailUrl,
            updatedAt = updatedAt,
            createdAt = createdAt,
            artists = artists,
            releaseDate = releasedDate,
            trackCount = trackCount,
            tracks = tracks
        )
    }

    companion object {
        fun fromDomain(domain: Album): AlbumDto {
            return AlbumDto(
                id = domain.id,
                pid = domain.platformId,
                platform = domain.platform.name,
                name = domain.name,
                thumbnailUrl = domain.thumbnailUrl,
                artists = domain.artists,
                tracks = domain.tracks,
                releasedDate = domain.releaseDate,
                trackCount = domain.trackCount
            )
        }
    }
}