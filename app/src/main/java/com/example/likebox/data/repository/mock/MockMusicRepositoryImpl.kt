package com.example.likebox.data.repository.mock

import androidx.annotation.RequiresApi
import com.example.likebox.domain.model.library.*
import com.example.likebox.domain.repository.MusicRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class MockMusicRepositoryImpl @Inject constructor(): MusicRepository {
    private val likedContent = ConcurrentHashMap<Pair<MusicPlatform, ContentType>, MutableList<MusicContent>>()
    private val tracks = ConcurrentHashMap<String, Track>()
    private val albums = ConcurrentHashMap<String, Album>()
    private val playlists = ConcurrentHashMap<String, Playlist>()
    private val artists = ConcurrentHashMap<String, Artist>()
    private val recentContents = mutableListOf<MusicContent>()

    override suspend fun getLikedContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<List<MusicContent>> = runCatching {
        likedContent[platform to contentType]?.toList() ?: emptyList()
    }

    override suspend fun addToLiked(content: MusicContent): Result<Unit> = runCatching {
        val key = content.platform to when(content) {
            is Track -> ContentType.TRACK
            is Album -> ContentType.ALBUM
            is Playlist -> ContentType.PLAYLIST
            is Artist -> ContentType.ARTIST
            else -> throw IllegalArgumentException("Unknown content type")
        }
        likedContent.getOrPut(key) { mutableListOf() }.add(content)
    }

    override suspend fun removeFromLiked(content: MusicContent): Result<Unit> = runCatching {
        val key = content.platform to when(content) {
            is Track -> ContentType.TRACK
            is Album -> ContentType.ALBUM
            is Playlist -> ContentType.PLAYLIST
            is Artist -> ContentType.ARTIST
            else -> throw IllegalArgumentException("Unknown content type")
        }
        likedContent[key]?.remove(content)
    }

    override suspend fun syncContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Unit> = runCatching {
        Unit
    }

    override suspend fun getTracks(platforms: Set<MusicPlatform>): Result<List<Track>> = runCatching {
        tracks.values.filter { it.platform in platforms }.toList()
    }

    override suspend fun getAlbums(platforms: Set<MusicPlatform>): Result<List<Album>> = runCatching {
        albums.values.filter { it.platform in platforms }.toList()
    }

    override suspend fun getPlaylists(platforms: Set<MusicPlatform>): Result<List<Playlist>> = runCatching {
        playlists.values.filter { it.platform in platforms }.toList()
    }

    override suspend fun getArtists(platforms: Set<MusicPlatform>): Result<List<Playlist>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlbumById(albumId: String): Result<Album> = runCatching {
        albums[albumId] ?: throw NoSuchElementException("Album not found: $albumId")
    }

    override suspend fun getPlaylistById(playlistId: String): Result<Playlist> = runCatching {
        playlists[playlistId] ?: throw NoSuchElementException("Playlist not found: $playlistId")
    }

    override suspend fun getArtistById(artistId: String): Result<Artist> = runCatching {
        artists[artistId] ?: throw NoSuchElementException("Artist not found: $artistId")
    }

    override suspend fun getContentCount(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Int> = runCatching {
        likedContent[platform to contentType]?.size ?: 0
    }

    override suspend fun clearLocalCache(): Result<Unit> = runCatching {
        likedContent.clear()
        tracks.clear()
        albums.clear()
        playlists.clear()
        artists.clear()
        recentContents.clear()
    }

    override suspend fun searchContent(
        query: String,
        contentType: ContentType,
        platforms: Set<MusicPlatform>
    ): Result<List<MusicContent>> = runCatching {
        val contents = when (contentType) {
            ContentType.TRACK -> tracks.values
            ContentType.ALBUM -> albums.values
            ContentType.PLAYLIST -> playlists.values
            ContentType.ARTIST -> artists.values
        }

        contents.filter { content ->
            content.platform in platforms &&
                    content.name.contains(query, ignoreCase = true)
        }.toList()
    }

    override suspend fun getPlaylist(playlistId: String): Any =
        playlists[playlistId] ?: throw NoSuchElementException("Playlist not found: $playlistId")

    override suspend fun getRecentContents(): List<MusicContent> = recentContents.toList()

    override suspend fun getAlbumTracks(albumId: String): Result<List<Track>> = runCatching {
        albums[albumId]?.tracks ?: throw NoSuchElementException("Album not found: $albumId")
    }

    // 테스트 헬퍼 메서드들
    @RequiresApi(35)
    fun addTestContent(content: MusicContent) {
        when (content) {
            is Track -> tracks[content.id] = content
            is Album -> albums[content.id] = content
            is Playlist -> playlists[content.id] = content
            is Artist -> artists[content.id] = content
        }
        addRecentContent(content)
    }

    @RequiresApi(35)
    private fun addRecentContent(content: MusicContent) {
        recentContents.add(0, content)
        if (recentContents.size > 20) {
            recentContents.removeLast()
        }
    }

    fun clear() {
        tracks.clear()
        albums.clear()
        playlists.clear()
        artists.clear()
        likedContent.clear()
        recentContents.clear()
    }
}