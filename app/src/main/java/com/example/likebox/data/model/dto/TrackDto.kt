package com.example.likebox.data.model.dto

import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.Track

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

        fun fromMap(map: Map<String, Any>): TrackDto {
            return TrackDto(
                id = (map["id"] as? String) ?: "",
                pid = (map["pid"] as? String) ?: "",
                platform = (map["platform"] as? String) ?: "",
                name = (map["name"] as? String) ?: "",
                thumbnailUrl = (map["coverImageUrl"] as? String) ?: "",
                artists = (map["artists"] as? List<String>) ?: emptyList(),
                albumName = (map["albumName"] as? String) ?: "",
                durationMs = (map["durationMs"] as? Number)?.toInt() ?: 0
            )
        }
    }
}