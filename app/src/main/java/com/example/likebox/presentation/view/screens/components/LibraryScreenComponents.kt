package com.example.likebox.presentation.view.screens.components

import com.example.likebox.domain.model.*
import java.time.Instant

public fun getDummyContent(contentType: ContentType): List<MusicContent> {
    return when (contentType) {
        ContentType.TRACK -> listOf(
            Track(
                id = "1",
                platformId = "spotify_1",
                platform = MusicPlatform.SPOTIFY,
                name = "Dynamite",
                thumbnailUrl = "https://example.com/dynamite.jpg",
                artists = listOf("BTS"),
                album = "BE",
                durationMs = 199054,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            ),
            Track(
                id = "2",
                platformId = "apple_1",
                platform = MusicPlatform.APPLE_MUSIC,
                name = "Butter",
                thumbnailUrl = "https://example.com/butter.jpg",
                artists = listOf("BTS"),
                album = "Butter",
                durationMs = 164000,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            ),
            Track(
                id = "3",
                platformId = "spotify_2",
                platform = MusicPlatform.SPOTIFY,
                name = "Spring Day",
                thumbnailUrl = "https://example.com/springday.jpg",
                artists = listOf("BTS"),
                album = "You Never Walk Alone",
                durationMs = 285000,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            )
        )
        ContentType.ALBUM -> listOf(
            Album(
                id = "1",
                platformId = "spotify_album_1",
                platform = MusicPlatform.SPOTIFY,
                name = "BE",
                thumbnailUrl = "https://example.com/be_album.jpg",
                artists = listOf("BTS"),
                releaseDate = Instant.now().epochSecond,
                trackCount = 8,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            ),
            Album(
                id = "2",
                platformId = "apple_album_1",
                platform = MusicPlatform.APPLE_MUSIC,
                name = "Map of the Soul: 7",
                thumbnailUrl = "https://example.com/mots7_album.jpg",
                artists = listOf("BTS"),
                releaseDate = Instant.now().epochSecond,
                trackCount = 20,
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            )
        )
        ContentType.PLAYLIST -> listOf(
            Playlist(
                id = "1",
                platformId = "spotify_playlist_1",
                platform = MusicPlatform.SPOTIFY,
                name = "BTS Hits",
                thumbnailUrl = "https://example.com/playlist1.jpg",
                description = "Best BTS songs",
                trackCount = 50,
                owner = "User123",
                tracks = emptyList(),
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            ),
            Playlist(
                id = "2",
                platformId = "apple_playlist_1",
                platform = MusicPlatform.APPLE_MUSIC,
                name = "K-pop Favorites",
                thumbnailUrl = "https://example.com/playlist2.jpg",
                description = "Popular K-pop songs",
                trackCount = 100,
                owner = "User456",
                tracks = emptyList(),
                createdAt = Instant.now().epochSecond,
                updatedAt = Instant.now().epochSecond
            )
        )
    }
}