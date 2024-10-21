package com.example.likebox.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class MusicPlatformSyncTest {

    private lateinit var spotifyRepository: MusicPlatformRepository
    private lateinit var appleMusicRepository: MusicPlatformRepository
    private lateinit var musicPlatformSync: MusicPlatformSync

    @Before
    fun setup() {
        spotifyRepository = mock(MusicPlatformRepository::class.java)
        appleMusicRepository = mock(MusicPlatformRepository::class.java)
        musicPlatformSync = MusicPlatformSync(spotifyRepository, appleMusicRepository)
    }

    @Test
    fun `sync liked songs from multiple platforms`() = runBlocking {
        // Given
        val spotifyLikedSongs = listOf(
            Song("1", "Song 1", "Artist 1"),
            Song("2", "Song 2", "Artist 2")
        )
        val appleMusicLikedSongs = listOf(
            Song("3", "Song 3", "Artist 3"),
            Song("2", "Song 2", "Artist 2")  // Duplicate song
        )

        `when`(spotifyRepository.getLikedSongs()).thenReturn(spotifyLikedSongs)
        `when`(appleMusicRepository.getLikedSongs()).thenReturn(appleMusicLikedSongs)

        // When
        val syncedSongs = musicPlatformSync.syncLikedSongs()

        // Then
        assertEquals(3, syncedSongs.size)
        assertEquals(setOf("1", "2", "3"), syncedSongs.map { it.id }.toSet())
        verify(spotifyRepository).getLikedSongs()
        verify(appleMusicRepository).getLikedSongs()
    }
}