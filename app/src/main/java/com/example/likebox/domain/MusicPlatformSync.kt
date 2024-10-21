package com.example.likebox.domain

class MusicPlatformSync(
    private val spotifyRepository: MusicPlatformRepository,
    private val appleMusicRepository: MusicPlatformRepository
) {
    suspend fun syncLikedSongs(): List<Song> {
        val spotifySongs = spotifyRepository.getLikedSongs()
        val appleMusicSongs = appleMusicRepository.getLikedSongs()

        return (spotifySongs + appleMusicSongs).distinctBy { it.id }
    }
}

interface MusicPlatformRepository {
    suspend fun getLikedSongs(): List<Song>
}

data class Song(
    val id: String,
    val title: String,
    val artist: String
)