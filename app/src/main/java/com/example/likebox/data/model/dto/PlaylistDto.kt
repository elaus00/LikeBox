package com.example.likebox.data.model.dto

import com.example.likebox.domain.model.library.MusicPlatform
import com.example.likebox.domain.model.library.Playlist
import com.example.likebox.domain.model.library.Track

data class PlaylistDto(
    override val id: String,
    override val pid: String,
    override val platform: String,
    override val name: String,
    override val thumbnailUrl: String,
    val description: String?,
    val tracks: List<Track>,
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

        fun fromMap(map: Map<String, Any>): PlaylistDto {
            return PlaylistDto(
                id = (map["id"] as? String) ?: "",
                pid = (map["pid"] as? String) ?: "",
                platform = (map["platform"] as? String) ?: "",
                name = (map["name"] as? String) ?: "",
                thumbnailUrl = (map["coverImageUrl"] as? String) ?: "",
                description = (map["description"] as? String) ?: "",
                tracks = (map["tracks"] as? List<Map<String, Any>>)?.map { item ->
                    TrackDto.fromMap(item).toDomain()
                } ?: emptyList(),
                owner = (map["owner"] as? String) ?: "",
                trackCount = (map["trackCount"] as? Number)?.toInt() ?: 0
            )
        }
    }
}