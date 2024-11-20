package com.example.likebox.data.model.dto

import com.example.likebox.domain.model.library.Artist
import com.example.likebox.domain.model.library.MusicPlatform

data class ArtistDto(
    override val id: String,
    override val pid: String,
    override val platform: String,
    override val name: String,
    override val thumbnailUrl: String,
    val genres: List<String>,
    val followerCount: Int,
    val externalUrl: String,
    val popularity: Int
) : MusicContentDto<Artist> {

    override fun toDomain(createdAt: Long, updatedAt: Long): Artist {
        return Artist(
            id = id,
            platformId = pid,
            platform = MusicPlatform.fromId(platform),
            name = name,
            thumbnailUrl = thumbnailUrl,
            updatedAt = updatedAt,
            createdAt = createdAt,
            genres = genres,
            followerCount = followerCount,
            externalUrl = externalUrl,
            popularity = popularity
        )
    }

    companion object {
        fun fromDomain(domain: Artist): ArtistDto {
            return ArtistDto(
                id = domain.id,
                pid = domain.platformId,
                platform = domain.platform.name,
                name = domain.name,
                thumbnailUrl = domain.thumbnailUrl,
                genres = domain.genres,
                followerCount = domain.followerCount,
                externalUrl = domain.externalUrl,
                popularity = domain.popularity
            )
        }

        fun fromMap(map: Map<String, Any>): ArtistDto {
            return ArtistDto(
                id = (map["id"] as? String) ?: "",
                pid = (map["pid"] as? String) ?: "",
                platform = (map["platform"] as? String) ?: "",
                name = (map["name"] as? String) ?: "",
                thumbnailUrl = (map["coverImageUrl"] as? String) ?: "",
                genres = (map["genres"] as? List<String>) ?: emptyList(),
                followerCount = (map["followerCount"] as? Number)?.toInt() ?: 0,
                externalUrl = (map["externalUrl"] as? String) ?: "",
                popularity = (map["popularity"] as? Number)?.toInt() ?: 0
            )
        }
    }
}