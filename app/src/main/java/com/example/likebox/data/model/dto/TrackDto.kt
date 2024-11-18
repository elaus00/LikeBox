package com.example.likebox.data.model.dto

import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.model.Track
import com.google.gson.annotations.SerializedName

data class TrackDto(
    override val id: String,
    override val pid: String,
    override val platform: String,
    override val name: String,
    override val thumbnailUrl: String,
    val artists: List<String>,
    val albumName: String,
    val durationMs: Int
) : MusicContentDto<Track> {

    override fun toDomain(createdAt: Long, updatedAt: Long): Track {
        return Track(
            id = id,
            platformId = pid,
            platform = MusicPlatform.fromId(platform),
            name = name,
            thumbnailUrl = thumbnailUrl,
            createdAt = createdAt,
            updatedAt = updatedAt,
            artists = artists,
            album = albumName,
            durationMs = durationMs
        )
    }

    companion object {
        fun fromDomain(domain: Track): TrackDto {
            return TrackDto(
                id = domain.id,
                pid = domain.platformId,
                platform = domain.platform.name,
                name = domain.name,
                thumbnailUrl = domain.thumbnailUrl,
                artists = domain.artists,
                albumName = domain.album,
                durationMs = domain.durationMs
            )
        }
    }
}