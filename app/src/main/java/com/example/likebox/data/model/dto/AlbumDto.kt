package com.example.likebox.data.model.dto

import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.Album
import com.example.likebox.domain.model.library.Track

data class AlbumDto(
    override val id: String,
    override val pid: String,
    override val platform: String,
    override val name: String,
    override val thumbnailUrl: String,
    val artists: List<String>,
    val tracks: List<Track>,
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

        fun fromMap(map: Map<String, Any>): AlbumDto {
            return AlbumDto(
                id = (map["id"] as? String) ?: "",
                pid = (map["pid"] as? String) ?: "",
                platform = (map["platform"] as? String) ?: "",
                name = (map["name"] as? String) ?: "",
                thumbnailUrl = (map["coverImageUrl"] as? String) ?: "",
                artists = (map["artists"] as? List<String>) ?: emptyList(),
                tracks = (map["tracks"] as? List<Map<String, Any>>)?.map { item ->
                    TrackDto.fromMap(item).toDomain()
                } ?: emptyList(),
                releasedDate = (map["releasedDate"] as? Number)?.toLong() ?: 0L,
                trackCount = (map["trackCount"] as? Number)?.toInt() ?: 0
            )
        }
    }
}