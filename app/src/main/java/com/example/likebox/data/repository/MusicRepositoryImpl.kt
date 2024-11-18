package com.example.likebox.data.repository

import com.example.likebox.data.firebase.FirebaseService
import com.example.likebox.domain.model.Track
import com.example.likebox.domain.model.Playlist
import com.example.likebox.domain.model.Album
import com.example.likebox.domain.model.Artist
import com.example.likebox.domain.model.ContentType
import com.example.likebox.domain.model.MusicContent
import com.example.likebox.domain.model.MusicPlatform
import com.example.likebox.domain.repository.MusicRepository

import com.example.likebox.data.model.dto.AlbumDto
import com.example.likebox.data.model.dto.PlaylistDto
import com.example.likebox.data.model.dto.TrackDto
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class MusicRepositoryImpl @Inject constructor(
    private val firebaseService: FirebaseService,
    private val functions: FirebaseFunctions
) : MusicRepository {
    override suspend fun getLikedContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<List<MusicContent>> {
        return try {
            val data = mapOf(
                "platform" to platform.name,
                "type" to contentType.name
            )
            val response = functions
                .getHttpsCallable("getLikedContent")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch liked content"))
            }

            val contentList = result["data"] as? List<Map<String, Any>> ?: emptyList()

            if (contentList.isEmpty()) {
                return Result.success(emptyList())
            }

            val contents = when (contentType) {
                ContentType.TRACK -> contentList.map { item ->
                    TrackDto(
                        id = (item["id"] as? String) ?: "",
                        pid = (item["pid"] as? String) ?: "",
                        platform = (item["platform"] as? String) ?: "",
                        name = (item["name"] as? String) ?: "",
                        thumbnailUrl = (item["albumArtUrl"] as? String) ?: "",
                        artists = (item["artist"] as? List<String>) ?: emptyList(),
                        albumName = (item["albumName"] as? String) ?: "",
                        durationMs = (item["durationMs"] as? Number)?.toInt() ?: 0
                    ).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                }
                ContentType.PLAYLIST -> contentList.map { item ->
                    PlaylistDto(
                        id = (item["id"] as? String) ?: "",
                        pid = (item["pid"] as? String) ?: "",
                        platform = (item["platform"] as? String) ?: "",
                        name = (item["name"] as? String) ?: "",
                        thumbnailUrl = (item["coverImageUrl"] as? String) ?: "",
                        description = item["description"] as? String,
                        tracks = (item["tracks"] as? List<String>) ?: emptyList(),
                        owner = (item["owner"] as? String) ?: "",
                        trackCount = (item["trackCount"] as? Number)?.toInt() ?: 0
                    ).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                }
                ContentType.ALBUM -> contentList.map { item ->
                    AlbumDto(
                        id = (item["id"] as? String) ?: "",
                        pid = (item["pid"] as? String) ?: "",
                        platform = (item["platform"] as? String) ?: "",
                        name = (item["name"] as? String) ?: "",
                        thumbnailUrl = (item["coverImageUrl"] as? String) ?: "",
                        artists = (item["artists"] as? List<String>) ?: emptyList(),
                        tracks = (item["tracks"] as? List<String>) ?: emptyList(),
                        releasedDate = (item["releasedDate"] as? Number)?.toLong() ?: 0L,
                        trackCount = (item["trackCount"] as? Number)?.toInt() ?: 0
                    ).toDomain(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                }
            }

            Result.success(contents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addToLiked(content: MusicContent): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromLiked(content: MusicContent): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun syncContent(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Unit> {
        return try {
            val data = mapOf(
                "platform" to platform.name,
                "contentType" to contentType.name
            )

            val response = functions
                .getHttpsCallable("synchContent")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            val success = result["success"] as? Boolean ?: false
            val message = result["message"] as? String ?: "Unknown error occurred"

            if (!success) {
                // 토큰 만료 에러 체크
                if (message.contains("Access token is expired")) {
                    return Result.failure(Exception(message))
                }
                return Result.failure(Exception(message))
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTracks(platforms: Set<MusicPlatform>): Result<List<Track>> {
        return try {
            val data = mapOf(
                "platforms" to platforms.map { it.name }
            )

            val response = functions
                .getHttpsCallable("getPlatformsTracks")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch platform tracks"))
            }

            val trackList = result["data"] as? List<Map<String, Any>> ?: run {
                return Result.failure(Exception("Invalid track list format"))
            }

            val tracks = trackList.map { item ->
                TrackDto(
                    id = (item["id"] as? String) ?: "",
                    pid = (item["pid"] as? String) ?: "",
                    platform = (item["platform"] as? String) ?: "",
                    name = (item["name"] as? String) ?: "",
                    thumbnailUrl = (item["albumArtUrl"] as? String) ?: "",
                    artists = (item["artist"] as? List<String>) ?: emptyList(),
                    albumName = (item["albumName"] as? String) ?: "",
                    durationMs = (item["durationMs"] as? Number)?.toInt() ?: 0
                ).toDomain(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }

            Result.success(tracks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlbums(platforms: Set<MusicPlatform>): Result<List<Album>> {
        return try{

            val data = mapOf(
                "platforms" to platforms.map { it.name }
            )

            val response = functions
                .getHttpsCallable("getPlatformsAlbums")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch platform tracks"))
            }

            val albumList = result["data"] as? List<Map<String, Any>> ?: run {
                return Result.failure(Exception("Invalid track list format"))
            }

            val albums = albumList.map { item ->
                AlbumDto(
                    id = (item["id"] as? String) ?: "",
                    pid = (item["pid"] as? String) ?: "",
                    platform = (item["platform"] as? String) ?: "",
                    name = (item["name"] as? String) ?: "",
                    thumbnailUrl = (item["albumArtUrl"] as? String) ?: "",
                    artists = (item["artist"] as? List<String>) ?: emptyList(),
                    tracks = (item["tracks"] as? List<String>) ?: emptyList(),
                    releasedDate = (item["releasedDate"] as? Number)?.toLong() ?: 0L,
                    trackCount = (item["trackCount"] as? Number)?.toInt() ?: 0
                ).toDomain(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }

            Result.success(albums)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaylists(platforms: Set<MusicPlatform>): Result<List<Playlist>> {
        return try{

            val data = mapOf(
                "platforms" to platforms.map { it.name }
            )

            val response = functions
                .getHttpsCallable("getPlatformsPlaylists")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch platform tracks"))
            }

            val playlistList = result["data"] as? List<Map<String, Any>> ?: run {
                return Result.failure(Exception("Invalid track list format"))
            }

            val playlists = playlistList.map { item ->
                PlaylistDto(
                    id = (item["id"] as? String) ?: "",
                    pid = (item["pid"] as? String) ?: "",
                    platform = (item["platform"] as? String) ?: "",
                    name = (item["name"] as? String) ?: "",
                    thumbnailUrl = (item["albumArtUrl"] as? String) ?: "",
                    description = (item["description"] as? String) ?: "",
                    trackCount = (item["trackCount"] as? Number)?.toInt() ?: 0,
                    owner = (item["owner"] as? String) ?: "",
                    tracks = (item["tracks"] as? List<String>) ?: emptyList()
                ).toDomain(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }

            Result.success(playlists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlbumById(albumId: String): Result<Album> {
        return try{

            val data = mapOf(
                "albumId" to albumId
            )

            val response = functions
                .getHttpsCallable("getAlbum")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch platform tracks"))
            }

            val item = result["data"] as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid track list format"))
            }

            val album = AlbumDto(
                    id = (item["id"] as? String) ?: "",
                    pid = (item["pid"] as? String) ?: "",
                    platform = (item["platform"] as? String) ?: "",
                    name = (item["name"] as? String) ?: "",
                    thumbnailUrl = (item["albumArtUrl"] as? String) ?: "",
                    artists = (item["artist"] as? List<String>) ?: emptyList(),
                    tracks = (item["tracks"] as? List<String>) ?: emptyList(),
                    releasedDate = (item["releasedDate"] as? Number)?.toLong() ?: 0L,
                    trackCount = (item["trackCount"] as? Number)?.toInt() ?: 0
                ).toDomain(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

            Result.success(album)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaylistById(playlistId: String): Result<Playlist> {
        return try{

            val data = mapOf(
                "playlistId" to playlistId
            )

            val response = functions
                .getHttpsCallable("getPlaylist")
                .call(data)
                .await()

            val result = response.getData() as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid response format"))
            }

            if (result["success"] == false) {
                return Result.failure(Exception("Failed to fetch platform tracks"))
            }

            val item = result["data"] as? Map<String, Any> ?: run {
                return Result.failure(Exception("Invalid track list format"))
            }

            val playlist = PlaylistDto(
                id = (item["id"] as? String) ?: "",
                pid = (item["pid"] as? String) ?: "",
                platform = (item["platform"] as? String) ?: "",
                name = (item["name"] as? String) ?: "",
                thumbnailUrl = (item["albumArtUrl"] as? String) ?: "",
                description = (item["description"] as? String) ?: "",
                trackCount = (item["trackCount"] as? Number)?.toInt() ?: 0,
                owner = (item["owner"] as? String) ?: "",
                tracks = (item["tracks"] as? List<String>) ?: emptyList()
            ).toDomain(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            Result.success(playlist)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArtistById(artistId: String): Result<Artist> {
        TODO("Not yet implemented")
    }

    override suspend fun getArtistTracks(artistId: String): Result<List<Track>> {
        TODO("Not yet implemented")
    }

    override suspend fun getArtistAlbums(artistId: String): Result<List<Album>> {
        TODO("Not yet implemented")
    }

    override suspend fun getContentCount(
        platform: MusicPlatform,
        contentType: ContentType
    ): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun clearLocalCache(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun searchContent(
        query: String,
        contentType: ContentType,
        platforms: Set<MusicPlatform>
    ): Result<List<MusicContent>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaylist(playlistId: String): Any {
        TODO("Not yet implemented")
    }
}